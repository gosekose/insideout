package com.insideout.job

import com.insideout.base.SoftDeleteStatus
import com.insideout.jdbc.MemoryMarbleJdbcRepository
import com.insideout.job.MemoryMarbleDailyToPermanentUpdateJobConfig.Companion.JOB_NAME
import com.insideout.listener.BatchJobExecutionListener
import com.insideout.listener.BatchStepExecutionListener
import com.insideout.memoryMarble.model.MemoryMarbleJpaEntity
import com.insideout.memoryMarble.model.model.MemoryMarbleContentJpaModel
import com.insideout.model.memoryMarble.type.StoreType
import com.insideout.partition.RangePartitioner
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JdbcPagingItemReader
import org.springframework.batch.item.database.Order
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.batch.BatchProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.jdbc.core.RowMapper
import org.springframework.transaction.PlatformTransactionManager
import java.sql.ResultSet
import java.sql.Timestamp
import java.time.Instant
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import javax.sql.DataSource

@Configuration
@ConditionalOnProperty(
    prefix = "spring.batch.job",
    name = ["name"],
    havingValue = JOB_NAME,
    matchIfMissing = true,
)
class MemoryMarbleDailyToPermanentUpdateJobConfig(
    private val jobRepository: JobRepository,
    private val batchProperties: BatchProperties,
    private val rangePartitioner: RangePartitioner,
    private val simpleAsyncTaskExecutor: SimpleAsyncTaskExecutor,
    private val batchJobExecutionListener: BatchJobExecutionListener,
    private val batchStepExecutionListener: BatchStepExecutionListener,
    private val memoryMarbleJdbcRepository: MemoryMarbleJdbcRepository,
    @Qualifier("businessDataSource") private val dataSource: DataSource,
    @Qualifier("businessTransactionManager") private val transactionManager: PlatformTransactionManager,
) {
    private val startOfLocalDateTime =
        Instant.now().truncatedTo(ChronoUnit.DAYS).atOffset(ZoneOffset.UTC).toLocalDateTime()
    private val startTimeStamp = Timestamp.valueOf(startOfLocalDateTime)
    private val endTimeStamp = Timestamp.valueOf(startOfLocalDateTime.plusDays(1))

    private val rowMapper =
        RowMapper { rs: ResultSet, _: Int ->
            MemoryMarbleJpaEntity(
                id = rs.getLong("id"),
                memberId = rs.getLong("member_id"),
                content = MemoryMarbleContentJpaModel(description = rs.getString("description")),
                feelingIds = rs.getString("feeling_ids").split(",").map { it.toLong() },
                storeType = StoreType.valueOf(rs.getString("store_type")),
                softDeleteStatus =
                    SoftDeleteStatus.valueOf(
                        rs.getString("status"),
                    ),
            ).apply {
                createdAt = rs.getTimestamp("created_at").toInstant()
                lastModifiedAt = rs.getTimestamp("last_modified_at").toInstant()
            }
        }

    @Bean
    fun memoryMarbleDailyToPermanentUpdaterJob(): Job {
        return JobBuilder(batchProperties.job.name, jobRepository)
            .incrementer(RunIdIncrementer())
            .start(primaryMemoryMarbleDailyToPermanentUpdaterStep())
            .listener(batchJobExecutionListener)
            .build()
    }

    @Bean
    fun primaryMemoryMarbleDailyToPermanentUpdaterStep(): Step {
        return StepBuilder("primaryMemoryMarbleDailyToPermanentUpdaterStep", jobRepository)
            .partitioner("replicaMemoryMarbleDailyToPermanentUpdaterStep", rangePartitioner)
            .step(replicaMemoryMarbleDailyToPermanentUpdaterStep())
            .partitionHandler(partitionHandler())
            .build()
    }

    @Bean
    fun replicaMemoryMarbleDailyToPermanentUpdaterStep(): Step {
        return StepBuilder("replicaMemoryMarbleDailyToPermanentUpdaterStep", jobRepository)
            .chunk<MemoryMarbleJpaEntity, MemoryMarbleJpaEntity>(1000, transactionManager)
            .reader(memoryMarbleReader())
            .processor(memoryMarbleProcessor())
            .writer(memoryMarbleWriter())
            .listener(batchStepExecutionListener)
            .allowStartIfComplete(true)
            .build()
    }

    @Bean
    fun partitionHandler(): TaskExecutorPartitionHandler {
        val partitionHandler = TaskExecutorPartitionHandler()
        partitionHandler.setTaskExecutor(simpleAsyncTaskExecutor)
        partitionHandler.step = replicaMemoryMarbleDailyToPermanentUpdaterStep()
        partitionHandler.gridSize = 5
        return partitionHandler
    }

    @Bean
    fun memoryMarbleReader(): JdbcPagingItemReader<MemoryMarbleJpaEntity> {
        val sortKeys = mapOf("id" to Order.ASCENDING)
        return JdbcPagingItemReaderBuilder<MemoryMarbleJpaEntity>()
            .dataSource(dataSource)
            .name("memoryMarbleJdbcPagingItemReader")
            .selectClause("SELECT * ")
            .fromClause("FROM memory_marbles")
            .whereClause("WHERE store_type = 'DAILY' where created_at > $startTimeStamp and created_at < $endTimeStamp")
            .sortKeys(sortKeys)
            .rowMapper(rowMapper)
            .pageSize(1000)
            .build()
    }

    @Bean
    fun memoryMarbleProcessor(): ItemProcessor<MemoryMarbleJpaEntity, MemoryMarbleJpaEntity> {
        return ItemProcessor { item ->
            item.apply {
                this.storeType = StoreType.PERMANENT
            }
        }
    }

    @Bean
    fun memoryMarbleWriter(): ItemWriter<MemoryMarbleJpaEntity> {
        return ItemWriter { items ->
            memoryMarbleJdbcRepository.saveInBatch(items.toList())
        }
    }

    companion object {
        const val JOB_NAME = "insideout.batch.memoryMarbleDailyToPermanentUpdater"
    }
}

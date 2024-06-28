package com.insideout.job

import com.insideout.jdbc.MemoryMarbleJdbcRepository
import com.insideout.jdbc.rowMapper.EntityRowMapper
import com.insideout.job.MemoryMarbleDailyToPermanentUpdateJobConfig.Companion.JOB_NAME
import com.insideout.listener.BatchJobExecutionListener
import com.insideout.listener.BatchStepExecutionListener
import com.insideout.memoryMarble.model.MemoryMarbleJpaEntity
import com.insideout.partition.RangePartitioner
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepScope
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
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.batch.BatchProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.PlatformTransactionManager
import java.sql.Timestamp
import java.time.Instant.now
import java.time.ZoneOffset.UTC
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
    private val jdbcTemplate: JdbcTemplate,
    private val jobRepository: JobRepository,
    private val batchProperties: BatchProperties,
    private val simpleAsyncTaskExecutor: SimpleAsyncTaskExecutor,
    private val batchJobExecutionListener: BatchJobExecutionListener,
    private val batchStepExecutionListener: BatchStepExecutionListener,
    private val memoryMarbleJdbcRepository: MemoryMarbleJdbcRepository,
    @Qualifier("businessDataSource") private val dataSource: DataSource,
    @Qualifier("businessTransactionManager") private val transactionManager: PlatformTransactionManager,
) {
    private val logger = LoggerFactory.getLogger(BatchJobExecutionListener::class.java)

    private val startOfLocalDateTime = now().truncatedTo(ChronoUnit.DAYS).atOffset(UTC).toLocalDateTime()
    private val startTimeStamp = Timestamp.valueOf(startOfLocalDateTime)
    private val endTimeStamp = Timestamp.valueOf(startOfLocalDateTime.plusDays(1))

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
            .partitioner("replicaMemoryMarbleDailyToPermanentUpdaterStep", rangePartitioner())
            .step(replicaMemoryMarbleDailyToPermanentUpdaterStep())
            .partitionHandler(partitionHandler())
            .build()
    }

    @Bean
    fun replicaMemoryMarbleDailyToPermanentUpdaterStep(): Step {
        return StepBuilder("replicaMemoryMarbleDailyToPermanentUpdaterStep", jobRepository)
            .chunk<MemoryMarbleJpaEntity, MemoryMarbleJpaEntity>(1000, transactionManager)
            .reader(memoryMarbleReader(null, null))
            .processor(memoryMarbleProcessor())
            .writer(memoryMarbleWriter())
            .listener(batchStepExecutionListener)
            .allowStartIfComplete(true)
            .build()
    }

    @Bean
    fun rangePartitioner(): RangePartitioner {
        val minSQL = """
            SELECT MIN(id)
            FROM memory_marbles
            WHERE store_type = 'DAILY'
            AND created_at >= '$startTimeStamp' AND created_at < '$endTimeStamp'
        """.trimIndent()

        val maxSQL = """
            SELECT MAX(id)
            FROM memory_marbles
            WHERE store_type = 'DAILY'
            AND created_at >= '$startTimeStamp' AND created_at < '$endTimeStamp'
        """.trimIndent()

        val minId = jdbcTemplate.queryForObject(minSQL, Long::class.java)
        val maxId = jdbcTemplate.queryForObject(maxSQL, Long::class.java)

        return RangePartitioner(
            minId = minId,
            maxId = maxId,
        )
    }


    @Bean
    fun partitionHandler(): TaskExecutorPartitionHandler {
        val partitionHandler = TaskExecutorPartitionHandler()
        partitionHandler.setTaskExecutor(simpleAsyncTaskExecutor)
        partitionHandler.step = replicaMemoryMarbleDailyToPermanentUpdaterStep()
        partitionHandler.gridSize = 5
        return partitionHandler
    }

    @StepScope
    @Bean
    fun memoryMarbleReader(
        @Value("#{stepExecutionContext['minValue']}") minValue: Long?,
        @Value("#{stepExecutionContext['maxValue']}") maxValue: Long?,
    ): JdbcPagingItemReader<MemoryMarbleJpaEntity> {
        val sortKeys = mapOf("id" to Order.ASCENDING)

        logger.info(
            """
                =================
                partition Ids = $minValue, $maxValue
                =================
            """.trimIndent()
        )

        return JdbcPagingItemReaderBuilder<MemoryMarbleJpaEntity>()
            .dataSource(dataSource)
            .name("memoryMarbleJdbcPagingItemReader")
            .selectClause("SELECT * ")
            .fromClause("FROM memory_marbles")
            .whereClause(
                "WHERE store_type = 'DAILY' " +
                        " AND created_at >= '$startTimeStamp' AND created_at < '$endTimeStamp'" +
                        " AND id BETWEEN $minValue AND $maxValue"
            )
            .sortKeys(sortKeys)
            .rowMapper(EntityRowMapper.memoryMarbleEntityRowMapper)
            .pageSize(1000)
            .build()
    }

    @Bean
    fun memoryMarbleProcessor(): ItemProcessor<MemoryMarbleJpaEntity, MemoryMarbleJpaEntity> {
        return ItemProcessor { item ->
            item
        }
    }

    @Bean
    fun memoryMarbleWriter(): ItemWriter<MemoryMarbleJpaEntity> {
        return ItemWriter { items ->
            memoryMarbleJdbcRepository.updateStoreTypeDailyToPermanentBatch(items.map { it.id })
        }
    }

    companion object {
        const val JOB_NAME = "insideout.batch.memoryMarbleDailyToPermanentUpdater"
    }
}

package com.insideout.job

import com.insideout.base.SoftDeleteStatus
import com.insideout.converter.ListLongToStringConverter
import com.insideout.job.MemoryMarbleStoreTypeDiscardRemoveJobConfig.Companion.JOB_NAME
import com.insideout.job.jdbc.MemoryMarbleJdbcRepository
import com.insideout.memoryMarble.model.MemoryMarbleJpaEntity
import com.insideout.memoryMarble.model.model.MemoryMarbleContentJpaModel
import com.insideout.model.memoryMarble.type.StoreType
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JdbcPagingItemReader
import org.springframework.batch.item.database.Order
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder
import org.springframework.boot.autoconfigure.batch.BatchProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.RowMapper
import org.springframework.transaction.PlatformTransactionManager
import java.sql.ResultSet

@Configuration
@ConditionalOnProperty(prefix = "spring.batch.job.name", havingValue = JOB_NAME, name = ["enabled"], matchIfMissing = true)
class MemoryMarbleStoreTypeDiscardRemoveJobConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val memoryMarbleJdbcRepository: MemoryMarbleJdbcRepository,
    private val batchProperties: BatchProperties,
) {
    private val rowMapper =
        RowMapper { rs: ResultSet, _: Int ->
            with(rs) {
                MemoryMarbleJpaEntity(
                    id = getLong("id"),
                    memberId = getLong("member_id"),
                    content = MemoryMarbleContentJpaModel(description = getString("description")),
                    feelingIds = ListLongToStringConverter().convertToEntityAttribute(getString("feeling_ids")),
                    storeType = getObject("store_type", StoreType::class.java),
                    softDeleteStatus = getObject("status", SoftDeleteStatus::class.java),
                ).apply {
                    createdAt = getTimestamp("create_at").toInstant()
                    lastModifiedAt = getTimestamp("last_modified_at").toInstant()
                }
            }
        }

    @Bean
    fun memoryMarbleDiscardRemoverJob(): Job {
        return JobBuilder(batchProperties.job.name, jobRepository)
            .start(memoryMarbleDiscardRemoverStep())
            .incrementer(RunIdIncrementer())
            .build()
    }

    @Bean
    fun memoryMarbleDiscardRemoverStep(): Step {
        return StepBuilder("memoryMarbleDiscardRemoverStep", jobRepository)
            .chunk<MemoryMarbleJpaEntity, MemoryMarbleJpaEntity>(100, transactionManager)
            .reader(memoryMarbleReader())
            .processor(memoryMarbleProcessor())
            .writer(memoryMarbleWriter())
            .allowStartIfComplete(true)
            .build()
    }

    @Bean
    fun memoryMarbleReader(): JdbcPagingItemReader<MemoryMarbleJpaEntity> {
        val sortKeys = mapOf("id" to Order.ASCENDING)
        return JdbcPagingItemReaderBuilder<MemoryMarbleJpaEntity>()
            .name("memoryMarbleJdbcPagingItemReader")
            .selectClause("SELECT * ")
            .fromClause("FROM memory_marbles")
            .whereClause("WHERE store_type = 'DISCARD'")
            .sortKeys(sortKeys)
            .rowMapper(rowMapper)
            .pageSize(100)
            .build()
    }

    @Bean
    fun memoryMarbleProcessor(): ItemProcessor<MemoryMarbleJpaEntity, MemoryMarbleJpaEntity> {
        return ItemProcessor { item -> item }
    }

    @Bean
    fun memoryMarbleWriter(): ItemWriter<MemoryMarbleJpaEntity> {
        return ItemWriter { items ->
            val idsToDelete = items.map { it.id }
            memoryMarbleJdbcRepository.deleteInBatchWithJdbc(idsToDelete)
        }
    }

    companion object {
        const val JOB_NAME = "insideout.batch.memoryMarbleDiscardRemover"
    }
}

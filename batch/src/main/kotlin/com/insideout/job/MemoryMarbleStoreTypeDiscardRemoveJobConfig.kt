package com.insideout.job

import com.insideout.job.MemoryMarbleStoreTypeDiscardRemoveJobConfig.Companion.JOB_NAME
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.batch.BatchProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate
import org.springframework.transaction.PlatformTransactionManager

@Configuration
@ConditionalOnProperty(
    prefix = "spring.batch.job.name",
    havingValue = JOB_NAME,
    name = ["enabled"],
    matchIfMissing = true
)
class MemoryMarbleStoreTypeDiscardRemoveJobConfig(
    private val jdbcTemplate: JdbcTemplate,
    private val jobRepository: JobRepository,
    private val batchProperties: BatchProperties,
    @Qualifier("businessTransactionManager") private val transactionManager: PlatformTransactionManager,
) {

    @Bean
    fun memoryMarbleDiscardRemoverJob(): Job {
        return JobBuilder(batchProperties.job.name, jobRepository)
            .incrementer(RunIdIncrementer())
            .start(memoryMarbleDiscardRemoverStep())
            .build()
    }

    @Bean
    fun memoryMarbleDiscardRemoverStep(): Step {
        return StepBuilder("memoryMarbleDiscardRemoverStep", jobRepository)
            .tasklet(deleteTasklet(), transactionManager)
            .build()
    }

    @Bean
    fun deleteTasklet(): Tasklet {
        return Tasklet { contribution, _ ->
            try {
                jdbcTemplate.update("DELETE FROM insideout.memory_marbles WHERE store_type = 'DISCARD'")
            } catch (e: Exception) {
                contribution.exitStatus = ExitStatus.FAILED
                throw e
            }
            RepeatStatus.FINISHED
        }
    }

    @Bean
    fun retryTemplate(): RetryTemplate {
        val retryTemplate = RetryTemplate()
        val retryPolicy = SimpleRetryPolicy()
        retryPolicy.maxAttempts = 3
        retryTemplate.setRetryPolicy(retryPolicy)
        return retryTemplate
    }

    companion object {
        const val JOB_NAME = "insideout.batch.memoryMarbleDiscardRemover"
    }
}

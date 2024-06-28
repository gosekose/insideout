package com.insideout.job.discardRemover

import com.insideout.job.discardRemover.MemoryMarbleStoreTypeDiscardRemoveJobConfig.Companion.JOB_NAME
import com.insideout.listener.BatchJobExecutionListener
import com.insideout.listener.BatchStepExecutionListener
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
    prefix = "spring.batch.job",
    name = ["name"],
    havingValue = JOB_NAME,
    matchIfMissing = true,
)
class MemoryMarbleStoreTypeDiscardRemoveJobConfig(
    private val jobRepository: JobRepository,
    private val batchProperties: BatchProperties,
    private val batchJobExecutionListener: BatchJobExecutionListener,
    @Qualifier("jdbcTemplate") private val jdbcTemplate: JdbcTemplate,
    @Qualifier("businessTransactionManager") private val transactionManager: PlatformTransactionManager,
) {
    @Bean
    fun memoryMarbleDiscardRemoverJob(): Job {
        return JobBuilder(batchProperties.job.name, jobRepository)
            .incrementer(RunIdIncrementer())
            .start(memoryMarbleDiscardRemoverStep())
            .listener(batchJobExecutionListener)
            .build()
    }

    @Bean
    fun memoryMarbleDiscardRemoverStep(): Step {
        return StepBuilder("memoryMarbleDiscardRemoverStep", jobRepository)
            .tasklet(deleteTasklet(), transactionManager)
            .listener(batchStepExecutionListener())
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

    @Bean
    fun batchStepExecutionListener(): BatchStepExecutionListener {
        return BatchStepExecutionListener()
    }

    companion object {
        const val JOB_NAME = "insideout.batch.memoryMarbleDiscardRemover"
    }
}

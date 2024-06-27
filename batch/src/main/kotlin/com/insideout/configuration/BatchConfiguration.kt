package com.insideout.configuration

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.batch.BatchProperties
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
@EnableBatchProcessing
@EnableConfigurationProperties(BatchProperties::class)
class BatchConfiguration(
    @Qualifier("batchDataSource") private val batchDataSource: DataSource,
) : DefaultBatchConfiguration() {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.batch.job", name = ["enabled"], havingValue = "true", matchIfMissing = true)
    fun jobLauncherApplicationRunner(
        jobLauncher: JobLauncher,
        jobExplorer: JobExplorer,
        jobRepository: JobRepository,
        properties: BatchProperties,
    ): JobLauncherApplicationRunner {
        val runner = JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository)
        val jobName: String? = properties.job.name
        if (!jobName.isNullOrEmpty()) {
            runner.setJobName(jobName)
        }

        return runner
    }


    @Bean
    fun jobRepository(@Qualifier("batchTransactionManager") transactionManager: PlatformTransactionManager): JobRepository {
        val factory = JobRepositoryFactoryBean()
        factory.setDataSource(batchDataSource)
        factory.transactionManager = transactionManager
        factory.afterPropertiesSet()
        return factory.getObject()
    }

    @Bean
    fun jobLauncher(jobRepository: JobRepository): JobLauncher {
        val jobLauncher = TaskExecutorJobLauncher()
        jobLauncher.setJobRepository(jobRepository)
        jobLauncher.setTaskExecutor(simpleAsyncTaskExecutor())
        jobLauncher.afterPropertiesSet()
        return jobLauncher
    }

    @Bean
    fun batchJobDuplicationChecker(): BatchJobDuplicationChecker = BatchJobDuplicationChecker()

    @Bean
    fun simpleAsyncTaskExecutor() = SimpleAsyncTaskExecutor()
}

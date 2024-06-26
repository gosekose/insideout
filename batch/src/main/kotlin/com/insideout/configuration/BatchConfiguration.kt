package com.insideout.configuration

import com.insideout.listener.BatchJobDuplicationChecker
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.boot.autoconfigure.batch.BatchProperties
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(BatchProperties::class)
class BatchConfiguration : DefaultBatchConfiguration() {
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
    fun batchJobDuplicationChecker(): BatchJobDuplicationChecker = BatchJobDuplicationChecker()
}

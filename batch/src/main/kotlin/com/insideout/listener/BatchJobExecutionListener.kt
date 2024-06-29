package com.insideout.listener

import com.insideout.configuration.BatchJobDuplicationChecker
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.batch.core.configuration.DuplicateJobException
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

@Component
class BatchJobExecutionListener(
    private val jobExplorer: JobExplorer,
    private val jobDuplicationChecker: BatchJobDuplicationChecker,
) : JobExecutionListener {
    override fun beforeJob(jobExecution: JobExecution) {
        require(jobDuplicationChecker.check(jobExplorer, jobExecution)) {
            throw DuplicateJobException("이미 실행 중인 Job이 존재합니다 [JobName: ${jobExplorer.jobNames}, JobId: ${jobExecution.jobId}]")
        }

        println(
            """
            ========================================================
            Job Name : ${jobExecution.jobInstance.jobName}, Job Id : ${jobExecution.jobId} Start      
            START_TIME = ${Instant.now().truncatedTo(ChronoUnit.DAYS).atOffset(ZoneOffset.UTC).toLocalDateTime()}
            ========================================================
            """.trimIndent(),
        )
        super.beforeJob(jobExecution)
    }

    override fun afterJob(jobExecution: JobExecution) {
        if (jobExecution.status == BatchStatus.FAILED) {
            println(
                """
                ========================================================
                Job Failed
                Job Name : ${jobExecution.jobInstance.jobName}, Job Id : ${jobExecution.jobId}
                ========================================================
                """.trimIndent(),
            )
        }

        if (jobExecution.status == BatchStatus.COMPLETED) {
            println(
                """
                ========================================================
                Job Completed
                Job Name : ${jobExecution.jobInstance.jobName}, Job Id : ${jobExecution.jobId}
                ========================================================
                """.trimIndent(),
            )
        }

        super.afterJob(jobExecution)
    }
}

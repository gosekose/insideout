package com.insideout.listener

import org.slf4j.LoggerFactory
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.batch.core.configuration.DuplicateJobException
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.stereotype.Component

@Component
class BatchJobExecutionListener(
    private val jobExplorer: JobExplorer,
    private val jobDuplicationChecker: BatchJobDuplicationChecker,
) : JobExecutionListener {
    private val logger = LoggerFactory.getLogger(BatchJobExecutionListener::class.java)
    override fun beforeJob(jobExecution: JobExecution) {
        require(jobDuplicationChecker.check(jobExplorer, jobExecution)) {
            throw DuplicateJobException("이미 실행 중인 Job이 존재합니다 [JobName: ${jobExplorer.jobNames}, JobId: ${jobExecution.jobId}]")
        }

        logger.info(
            """
            ========================================================
            Job Name : ${jobExecution.jobInstance.jobName}, Job Id : ${jobExecution.jobId} Start               
            ========================================================
            """.trimIndent(),
        )
        super.beforeJob(jobExecution)
    }

    override fun afterJob(jobExecution: JobExecution) {
        if (jobExecution.status == BatchStatus.FAILED) {
            logger.info(
                """
                ========================================================
                Job Failed
                Job Name : ${jobExecution.jobInstance.jobName}, Job Id : ${jobExecution.jobId}
                ========================================================
                """.trimIndent(),
            )
        }

        if (jobExecution.status == BatchStatus.COMPLETED) {
            logger.info(
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

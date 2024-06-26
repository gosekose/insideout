package com.insideout.listener

import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.explore.JobExplorer

class BatchJobDuplicationChecker {
    fun check(
        jobExplorer: JobExplorer,
        currentJobExecution: JobExecution,
    ): Boolean {
        val currentJobInstance = currentJobExecution.jobInstance
        // 생성순 내림차순 정렬해서 jobInstant 제공
        val jobInstances =
            jobExplorer.getJobInstances(
                currentJobInstance.jobName,
                0,
                10,
            )

        return jobInstances.any { jobInstance ->
            jobExplorer.getJobExecutions(jobInstance).any { jobExecution ->
                currentJobExecution.jobId != jobExecution.jobId && jobExecution.status.isRunning
            }
        }
    }
}

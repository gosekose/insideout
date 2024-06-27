package com.insideout.configuration

import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.explore.JobExplorer

class BatchJobDuplicationChecker {
    fun check(
        jobExplorer: JobExplorer,
        currentJobExecution: JobExecution,
    ): Boolean {
        val currentJobInstance = currentJobExecution.jobInstance
        val jobInstances = jobExplorer.getJobInstances(currentJobInstance.jobName, 0, 10)
        return jobInstances.any { jobInstance ->
            jobExplorer.getJobExecutions(jobInstance).any { jobExecution ->
                jobExecution.status.isRunning && jobExecution.jobParameters == currentJobExecution.jobParameters
            }
        }
    }
}

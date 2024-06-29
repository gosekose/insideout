package com.insideout.listener

import com.insideout.jdbc.FailedPartitionJdbcRepository
import com.insideout.jdbc.dao.FailedPartition
import org.slf4j.LoggerFactory
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.StepExecution

class PartitionStepExecutionListener(
    private val failedPartitionJdbcRepository: FailedPartitionJdbcRepository,
) : BatchStepExecutionListener() {
    private val logger = LoggerFactory.getLogger(PartitionStepExecutionListener::class.java)
    override fun addAfterStep(stepExecution: StepExecution) {
        if (stepExecution.exitStatus == ExitStatus.FAILED) {

            val minId = stepExecution.executionContext.getLong("minValue", -1L)
            val maxId = stepExecution.executionContext.getLong("maxValue", -1L)

            if (minId == -1L || maxId == -1L) return

            failedPartitionJdbcRepository.save(
                FailedPartition.of(
                    minId = minId,
                    maxId = maxId,
                    jobExecutionId = stepExecution.jobExecutionId,
                ),
            )
        }
    }
}

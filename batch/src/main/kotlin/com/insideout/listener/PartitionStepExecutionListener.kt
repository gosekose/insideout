package com.insideout.listener

import com.insideout.jdbc.FailedPartitionJdbcRepository
import com.insideout.jdbc.dao.FailedPartition
import com.insideout.job.dailyToPermanentUpdater.CustomJdbcPagingItemReader.Companion.FAIL_VALUE
import com.insideout.job.dailyToPermanentUpdater.MemoryMarbleDailyToPermanentUpdateJobConfig
import com.insideout.job.dailyToPermanentUpdater.MemoryMarbleDailyToPermanentUpdateJobConfig.Companion.MEMORY_MARBLE_JDBC_PAGING_ITEM_READER
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.StepExecution
import kotlin.math.min

class PartitionStepExecutionListener(
    private val failedPartitionJdbcRepository: FailedPartitionJdbcRepository,
) : BatchStepExecutionListener() {
    override fun addAfterStep(stepExecution: StepExecution) {
        val minId = stepExecution.executionContext.getLong("minValue", -1L)
        val maxId = stepExecution.executionContext.getLong("maxValue", -1L)

        if (minId == -1L || maxId == -1L) return

        if (stepExecution.exitStatus.exitCode == ExitStatus.FAILED.exitCode) {
            failedPartitionJdbcRepository.save(
                FailedPartition.of(
                    minId = minId,
                    maxId = maxId,
                    jobExecutionId = stepExecution.jobExecutionId,
                ),
            )
        } else if (stepExecution.exitStatus.exitCode == ExitStatus.COMPLETED.exitCode) {
            val failedValue =
                stepExecution.executionContext["$MEMORY_MARBLE_JDBC_PAGING_ITEM_READER.$FAIL_VALUE"] as? Map<String, String>?

            if (failedValue != null) {
                failedValue.keys.filter { it.contains("page_") }
                    .map { key ->
                        val page = key.substring(5).toInt()
                        val newMinId = minId + page * MemoryMarbleDailyToPermanentUpdateJobConfig.PAGE_SIZE
                        FailedPartition.of(
                            minId = newMinId,
                            maxId = min(maxId, newMinId + MemoryMarbleDailyToPermanentUpdateJobConfig.PAGE_SIZE),
                            jobExecutionId = stepExecution.jobExecutionId,
                        )
                    }.let { failedPartitionJdbcRepository.saveAll(it) }
            }
        }
    }
}

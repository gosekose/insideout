package com.insideout.listener

import com.insideout.jdbc.FailedPartitionJdbcRepository
import com.insideout.jdbc.dao.FailedPartition
import com.insideout.job.dailyToPermanentUpdater.MemoryMarbleDailyToPermanentUpdateJobConfig.Companion.MEMORY_MARBLE_JDBC_PAGING_ITEM_READER
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.StepExecution

class PartitionStepExecutionListener(
    private val failedPartitionJdbcRepository: FailedPartitionJdbcRepository,
) : BatchStepExecutionListener() {
    override fun addAfterStep(stepExecution: StepExecution) {
        println(stepExecution.executionContext.entrySet())
        if (stepExecution.exitStatus == ExitStatus.FAILED) {
            val minId = stepExecution.executionContext.getLong("minValue", -1L)
            val maxId = stepExecution.executionContext.getLong("maxValue", -1L)

            if (minId == -1L || maxId == -1L) return


            val value = stepExecution.executionContext["$MEMORY_MARBLE_JDBC_PAGING_ITEM_READER.$START_AFTER_VALUE"] as? Map<String?, Long?>
            if (value != null) {
                val startId = value["id"]
                if (startId != null) {
                    failedPartitionJdbcRepository.save(
                        FailedPartition.of(
                            minId = startId,
                            maxId = maxId,
                            jobExecutionId = stepExecution.jobExecutionId,
                        ),
                    )
                }
            }

        }
    }

    companion object {
        private const val START_AFTER_VALUE = "start.after"
    }

    data class StartId(
        val id: Long,
    )
}

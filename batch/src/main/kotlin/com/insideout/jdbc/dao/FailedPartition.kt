package com.insideout.jdbc.dao

import java.time.Instant

data class FailedPartition(
    val id: Long = 0L,
    val minId: Long,
    val maxId: Long,
    val stepExecutionId: Long,
    val createdAt: Instant,
    val lastModifiedAt: Instant,
    val status: Status,
) {
    enum class Status {
        FAILED,
        RECOVER_IN_PROGRESS,
        RECOVER_FAILED,
        RECOVER_SUCCESS,
    }

    companion object {
        fun of(
            minId: Long,
            maxId: Long,
            jobExecutionId: Long,
        ): FailedPartition {
            return FailedPartition(
                minId = minId,
                maxId = maxId,
                stepExecutionId = jobExecutionId,
                createdAt = Instant.now(),
                lastModifiedAt = Instant.now(),
                status = Status.FAILED,
            )
        }
    }
}

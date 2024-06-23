package com.insideout.feeling.repository

import com.insideout.base.SoftDeleteStatus
import com.insideout.feeling.model.FeelingJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FeelingJpaRepository : JpaRepository<FeelingJpaEntity, Long> {
    fun findByIdInAndSoftDeleteStatus(
        ids: List<Long>,
        softDeleteStatus: SoftDeleteStatus = SoftDeleteStatus.ACTIVE,
    ): List<FeelingJpaEntity>

    fun findByMemoryMarbleConnectMemoryMarbleIdAndSoftDeleteStatus(
        memoryMarbleConnectMemoryMarbleId: Long,
        softDeleteStatus: SoftDeleteStatus = SoftDeleteStatus.ACTIVE,
    ): List<FeelingJpaEntity>
}

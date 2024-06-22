package com.insideout.feeling.repository

import com.insideout.base.BaseJpaRepository
import com.insideout.base.SoftDeleteStatus
import com.insideout.feeling.model.FeelingJpaEntity

interface FeelingJpaRepository : BaseJpaRepository<FeelingJpaEntity, Long> {
    fun findByIdInAndSoftDeleteStatus(
        ids: List<Long>,
        softDeleteStatus: SoftDeleteStatus = SoftDeleteStatus.ACTIVE,
    ): List<FeelingJpaEntity>

    fun findByMemoryMarbleConnectMemoryMarbleIdAndSoftDeleteStatus(
        memoryMarbleConnectMemoryMarbleId: Long,
        softDeleteStatus: SoftDeleteStatus = SoftDeleteStatus.ACTIVE,
    ): List<FeelingJpaEntity>
}

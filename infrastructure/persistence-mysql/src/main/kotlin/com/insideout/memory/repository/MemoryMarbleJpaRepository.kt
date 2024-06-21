package com.insideout.memory.repository

import com.insideout.base.BaseJpaRepository
import com.insideout.base.SoftDeleteStatus
import com.insideout.memory.model.MemoryMarbleJpaEntity

interface MemoryMarbleJpaRepository : BaseJpaRepository<MemoryMarbleJpaEntity, Long> {
    fun findByIdInAndSoftDeleteStatus(
        ids: List<Long>,
        softDeleteStatus: SoftDeleteStatus = SoftDeleteStatus.ACTIVE,
    ): List<MemoryMarbleJpaEntity>
}

package com.insideout.memoryMarble.repository

import com.insideout.memoryMarble.model.MemoryMarbleJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemoryMarbleJpaRepository : JpaRepository<MemoryMarbleJpaEntity, Long>, MemoryMarbleQueryDslRepository {
    fun existsByIdGreaterThan(id: Long): Boolean
}

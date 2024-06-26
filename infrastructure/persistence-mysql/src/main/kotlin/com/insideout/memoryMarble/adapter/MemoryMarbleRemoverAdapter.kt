package com.insideout.memoryMarble.adapter

import com.insideout.annotation.WritableTransactional
import com.insideout.memoryMarble.repository.MemoryMarbleJpaRepository
import com.insideout.usecase.memoryMarble.port.MemoryMarbleRemover
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
@WritableTransactional
class MemoryMarbleRemoverAdapter(
    private val memoryMarbleJpaRepository: MemoryMarbleJpaRepository,
) : MemoryMarbleRemover {
    override fun remove(memoryMarbleId: Long) {
        val memoryMarbleJpaEntity = memoryMarbleJpaRepository.findByIdOrNull(memoryMarbleId) ?: return
        memoryMarbleJpaRepository.save(memoryMarbleJpaEntity.remove())
    }
}

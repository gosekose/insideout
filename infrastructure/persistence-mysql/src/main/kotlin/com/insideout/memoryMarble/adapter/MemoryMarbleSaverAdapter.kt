package com.insideout.memoryMarble.adapter

import com.insideout.annotation.WritableTransactional
import com.insideout.memoryMarble.model.MemoryMarbleJpaEntity
import com.insideout.memoryMarble.repository.MemoryMarbleJpaRepository
import com.insideout.model.memoryMarble.MemoryMarble
import com.insideout.usecase.memoryMarble.port.MemoryMarbleSaver
import org.springframework.stereotype.Repository

@Repository
@WritableTransactional
class MemoryMarbleSaverAdapter(
    private val memoryMarbleJpaRepository: MemoryMarbleJpaRepository,
) : MemoryMarbleSaver {
    override fun save(memoryMarble: MemoryMarble): MemoryMarble {
        return memoryMarbleJpaRepository.save(MemoryMarbleJpaEntity.from(memoryMarble))
            .toModel(memoryMarble.feelings)
    }
}

package com.insideout.memory.adapter

import com.insideout.annotation.WritableTransactional
import com.insideout.memory.model.MemoryMarbleJpaEntity
import com.insideout.memory.repository.MemoryMarbleJpaRepository
import com.insideout.model.memory.MemoryMarble
import com.insideout.usecase.memory.port.MemoryMarbleSaver
import org.springframework.stereotype.Component

@Component
@WritableTransactional
class MemoryMarbleSaverAdapter(
    private val memoryMarbleJpaRepository: MemoryMarbleJpaRepository,
) : MemoryMarbleSaver {
    override fun save(memoryMarble: MemoryMarble): MemoryMarble {
        return memoryMarbleJpaRepository.save(MemoryMarbleJpaEntity.from(memoryMarble))
            .toModel(memoryMarble.feelings)
    }
}

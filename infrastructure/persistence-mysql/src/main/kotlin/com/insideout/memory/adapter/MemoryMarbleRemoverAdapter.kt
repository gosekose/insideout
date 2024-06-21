package com.insideout.memory.adapter

import com.insideout.annotation.WritableTransactional
import com.insideout.base.notnull
import com.insideout.memory.repository.MemoryMarbleJpaRepository
import com.insideout.usecase.memory.RemoveMemoryMarbleUseCase
import com.insideout.usecase.memory.port.MemoryMarbleRemover
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
@WritableTransactional
class MemoryMarbleRemoverAdapter(
    private val memoryMarbleJpaRepository: MemoryMarbleJpaRepository,
) : MemoryMarbleRemover {
    override fun remove(command: RemoveMemoryMarbleUseCase.Command) {
        memoryMarbleJpaRepository.findByIdOrNull(command.id).notnull()
            .remove().let(memoryMarbleJpaRepository::save)
    }
}

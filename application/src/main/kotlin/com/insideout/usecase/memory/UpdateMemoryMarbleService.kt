package com.insideout.usecase.memory

import com.insideout.model.memory.MemoryMarble
import com.insideout.usecase.memory.port.MemoryMarbleUpdater
import org.springframework.stereotype.Component

@Component
class UpdateMemoryMarbleService(
    private val memoryMarbleUpdater: MemoryMarbleUpdater,
) : UpdateMemoryMarbleUseCase {
    override fun execute(command: UpdateMemoryMarbleUseCase.Command): MemoryMarble {
        return memoryMarbleUpdater.update(command)
    }
}

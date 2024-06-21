package com.insideout.usecase.memory

import com.insideout.model.memory.MemoryMarble
import com.insideout.usecase.memory.port.MemoryMarbleUpdater
import org.springframework.stereotype.Component

@Component
class UpdateMemoryMarbleStoreTypeService(
    private val memoryMarbleStoreUpdater: MemoryMarbleUpdater,
) : UpdateMemoryMarbleStoreTypeUseCase {
    override fun execute(command: UpdateMemoryMarbleStoreTypeUseCase.Command): MemoryMarble {
        return memoryMarbleStoreUpdater.update(command)
    }
}

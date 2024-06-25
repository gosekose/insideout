package com.insideout.usecase.memory

import com.insideout.exception.BusinessErrorCause.MEMORY_MARBLE_NOT_FOUND
import com.insideout.model.memory.MemoryMarble
import com.insideout.model.notnull
import com.insideout.usecase.memory.port.MemoryMarbleReader
import com.insideout.usecase.memory.port.MemoryMarbleSaver
import org.springframework.stereotype.Component

@Component
class UpdateMemoryMarbleStoreTypeService(
    private val memoryMarbleReader: MemoryMarbleReader,
    private val memoryMarbleSaver: MemoryMarbleSaver,
) : UpdateMemoryMarbleStoreTypeUseCase {
    override fun execute(command: UpdateMemoryMarbleStoreTypeUseCase.Command): MemoryMarble {
        val (id, storeType) = command
        val memoryMarble = memoryMarbleReader.getOrNull(id).notnull(MEMORY_MARBLE_NOT_FOUND)
        return memoryMarbleSaver.save(memoryMarble.update(storeType))
    }
}

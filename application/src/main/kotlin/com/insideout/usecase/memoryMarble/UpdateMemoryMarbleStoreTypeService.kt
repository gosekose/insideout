package com.insideout.usecase.memoryMarble

import com.insideout.exception.BusinessErrorCause.MEMORY_MARBLE_NOT_FOUND
import com.insideout.model.memoryMarble.MemoryMarble
import com.insideout.model.notnull
import com.insideout.usecase.memoryMarble.port.MemoryMarbleReader
import com.insideout.usecase.memoryMarble.port.MemoryMarbleSaver
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

package com.insideout.usecase.memoryMarble

import com.insideout.exception.BusinessErrorCause
import com.insideout.model.memoryMarble.MemoryMarble
import com.insideout.model.notnull
import com.insideout.usecase.memoryMarble.port.MemoryMarbleReader
import org.springframework.stereotype.Service

@Service
class GetMemoryMarbleByIdService(
    private val memoryMarbleReader: MemoryMarbleReader,
) : GetMemoryMarbleByIdUseCase {
    override fun execute(query: GetMemoryMarbleByIdUseCase.Query): MemoryMarble {
        return memoryMarbleReader.getOrNull(query.id).notnull(BusinessErrorCause.MEMORY_MARBLE_NOT_FOUND)
    }
}

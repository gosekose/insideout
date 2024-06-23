package com.insideout.usecase.memory

import com.insideout.model.memory.MemoryMarble
import com.insideout.model.notnull
import com.insideout.usecase.memory.port.MemoryMarbleReader
import org.springframework.stereotype.Service

@Service
class GetMemoryMarbleByIdService(
    private val memoryMarbleReader: MemoryMarbleReader,
) : GetMemoryMarbleByIdUseCase {
    override fun execute(query: GetMemoryMarbleByIdUseCase.Query): MemoryMarble {
        return memoryMarbleReader.getOrNull(query.id).notnull()
    }
}

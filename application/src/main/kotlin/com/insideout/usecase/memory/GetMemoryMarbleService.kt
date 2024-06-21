package com.insideout.usecase.memory

import com.insideout.model.memory.MemoryMarbles
import com.insideout.usecase.memory.port.MemoryMarbleReader
import org.springframework.stereotype.Component

@Component
class GetMemoryMarbleService(
    private val memoryMarbleReader: MemoryMarbleReader,
) : GetMemoryMarbleUseCase {
    override fun execute(query: GetMemoryMarbleUseCase.Query): MemoryMarbles {
        return memoryMarbleReader.getAll(query)
    }
}

package com.insideout.usecase.memory

import com.insideout.model.memory.MemoryMarble
import com.insideout.model.page.Pagination
import com.insideout.usecase.memory.port.MemoryMarbleReader
import org.springframework.stereotype.Component

@Component
class GetMemoryMarbleService(
    private val memoryMarbleReader: MemoryMarbleReader,
) : GetMemoryMarbleUseCase {
    override fun execute(query: GetMemoryMarbleUseCase.Query): Pagination<MemoryMarble> {
        val memoryMarbles = memoryMarbleReader.getAll(query)
        return Pagination(
            content = memoryMarbles,
            hasNext = memoryMarbles.lastOrNull()?.let { memoryMarbleReader.hasNext(it.id) } ?: false,
        )
    }
}

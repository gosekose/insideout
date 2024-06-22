package com.insideout.usecase.memory

import com.insideout.model.memory.MemoryMarble
import com.insideout.model.page.Pagination
import com.insideout.usecase.memory.port.MemoryMarbleReader
import org.springframework.stereotype.Component

@Component
class GetMemoryMarblesByPaginationService(
    private val memoryMarbleReader: MemoryMarbleReader,
) : GetMemoryMarblesByPaginationUseCase {
    override fun execute(query: GetMemoryMarblesByPaginationUseCase.Query): Pagination<MemoryMarble> {
        val memoryMarbles = memoryMarbleReader.getAll(query)
        return Pagination(
            content = memoryMarbles,
            hasNext = memoryMarbles.lastOrNull()?.let { memoryMarbleReader.hasNext(it.id) } ?: false,
        )
    }
}

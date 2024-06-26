package com.insideout.usecase.memoryMarble

import com.insideout.model.memoryMarble.MemoryMarble
import com.insideout.model.page.Pagination
import com.insideout.usecase.memoryMarble.port.MemoryMarbleReader
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

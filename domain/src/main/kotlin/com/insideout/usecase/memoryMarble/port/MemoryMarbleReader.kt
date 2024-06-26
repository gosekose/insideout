package com.insideout.usecase.memoryMarble.port

import com.insideout.model.memoryMarble.MemoryMarble
import com.insideout.model.memoryMarble.MemoryMarbles
import com.insideout.usecase.memoryMarble.GetMemoryMarblesByPaginationUseCase

interface MemoryMarbleReader {
    fun getOrNull(id: Long): MemoryMarble?

    fun getAll(query: GetMemoryMarblesByPaginationUseCase.Query): MemoryMarbles

    fun hasNext(currentId: Long): Boolean
}

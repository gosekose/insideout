package com.insideout.usecase.memory.port

import com.insideout.model.memory.MemoryMarble
import com.insideout.model.memory.MemoryMarbles
import com.insideout.usecase.memory.GetMemoryMarblesByPaginationUseCase

interface MemoryMarbleReader {
    fun getOrNull(id: Long): MemoryMarble?

    fun getAll(query: GetMemoryMarblesByPaginationUseCase.Query): MemoryMarbles

    fun hasNext(currentId: Long): Boolean
}

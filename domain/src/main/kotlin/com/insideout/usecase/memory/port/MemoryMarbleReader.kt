package com.insideout.usecase.memory.port

import com.insideout.model.memory.MemoryMarble
import com.insideout.model.memory.MemoryMarbles
import com.insideout.usecase.memory.GetMemoryMarbleUseCase

interface MemoryMarbleReader {
    fun getOrNull(id: Long): MemoryMarble?

    fun getAll(query: GetMemoryMarbleUseCase.Query): MemoryMarbles
}

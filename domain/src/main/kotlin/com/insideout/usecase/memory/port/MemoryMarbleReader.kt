package com.insideout.usecase.memory.port

import com.insideout.model.memory.MemoryMarble
import com.insideout.model.memory.MemoryMarbles

interface MemoryMarbleReader {
    fun getOrNull(id: Long): MemoryMarble?

    fun getAll(ids: List<Long>): MemoryMarbles
}

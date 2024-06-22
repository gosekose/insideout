package com.insideout.usecase.memory.port

import com.insideout.model.memory.MemoryMarble

interface MemoryMarbleSaver {
    fun save(memoryMarble: MemoryMarble): MemoryMarble
}

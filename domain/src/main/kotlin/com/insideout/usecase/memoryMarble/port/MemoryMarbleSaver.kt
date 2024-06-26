package com.insideout.usecase.memoryMarble.port

import com.insideout.model.memoryMarble.MemoryMarble

interface MemoryMarbleSaver {
    fun save(memoryMarble: MemoryMarble): MemoryMarble
}

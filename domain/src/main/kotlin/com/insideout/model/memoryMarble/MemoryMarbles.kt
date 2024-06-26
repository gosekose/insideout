package com.insideout.model.memoryMarble

data class MemoryMarbles(
    val marbles: List<MemoryMarble>,
) : List<MemoryMarble> by marbles

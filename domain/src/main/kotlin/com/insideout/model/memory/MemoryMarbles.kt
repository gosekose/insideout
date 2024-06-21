package com.insideout.model.memory

data class MemoryMarbles(
    val marbles: List<MemoryMarble>,
) : List<MemoryMarble> by marbles

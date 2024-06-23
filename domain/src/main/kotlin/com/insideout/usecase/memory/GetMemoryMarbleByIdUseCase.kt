package com.insideout.usecase.memory

import com.insideout.model.memory.MemoryMarble

interface GetMemoryMarbleByIdUseCase {
    fun execute(query: Query): MemoryMarble

    data class Query(
        val id: Long,
    )
}

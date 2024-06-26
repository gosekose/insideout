package com.insideout.usecase.memoryMarble

import com.insideout.model.memoryMarble.MemoryMarble

interface GetMemoryMarbleByIdUseCase {
    fun execute(query: Query): MemoryMarble

    data class Query(
        val id: Long,
    )
}

package com.insideout.usecase.memory

import com.insideout.model.memory.MemoryMarbles
import com.insideout.model.memory.type.StoreType

interface GetMemoryMarblesUseCase {
    fun execute(query: Query): MemoryMarbles

    data class Query(
        val memberId: Long,
        val storeType: StoreType,
    )
}

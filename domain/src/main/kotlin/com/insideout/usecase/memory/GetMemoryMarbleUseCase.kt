package com.insideout.usecase.memory

import com.insideout.model.memory.MemoryMarbles
import com.insideout.model.memory.type.StoreType

interface GetMemoryMarbleUseCase {
    fun execute(query: Query): MemoryMarbles

    data class Query(
        val memberId: Long,
        val storeType: StoreType,
        val offsetSearch: OffsetSearch,
    ) {
        data class OffsetSearch(
            val lastId: Long,
            val size: Long = 10L,
        )
    }
}

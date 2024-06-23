package com.insideout.usecase.memory

import com.insideout.model.memory.MemoryMarble
import com.insideout.model.memory.type.StoreType
import com.insideout.model.page.Pagination

interface GetMemoryMarblesByPaginationUseCase {
    fun execute(query: Query): Pagination<MemoryMarble>

    data class Query(
        val memberId: Long,
        val storeType: StoreType?,
        val offsetSearch: OffsetSearch,
    ) {
        data class OffsetSearch(
            val lastId: Long,
            val size: Long = 10L,
        )
    }
}

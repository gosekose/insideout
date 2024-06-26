package com.insideout.usecase.memoryMarble

import com.insideout.model.memoryMarble.MemoryMarble
import com.insideout.model.memoryMarble.type.StoreType
import com.insideout.model.page.Pagination

interface GetMemoryMarblesByPaginationUseCase {
    fun execute(query: Query): Pagination<MemoryMarble>

    data class Query(
        val memberId: Long,
        val storeType: StoreType?,
        val limitSearch: LimitSearch,
    ) {
        data class LimitSearch(
            val lastId: Long,
            val size: Long = 10L,
        )
    }
}

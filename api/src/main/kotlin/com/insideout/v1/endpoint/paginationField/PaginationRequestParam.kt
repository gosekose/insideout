package com.insideout.v1.endpoint.paginationField

data class PaginationRequestParam(
    val size: Long = 10L,
    val lastId: Long = 0L,
)

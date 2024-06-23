package com.insideout.v1.endpoint.paginationField

abstract class PaginationHttpResponse {
    abstract val hasNext: Boolean
    abstract val content: List<Any>
}

package com.insideout.v1.endpoint.memoryMarble.get

import com.insideout.model.memoryMarble.MemoryMarble
import com.insideout.model.memoryMarble.type.StoreType
import com.insideout.model.page.Pagination
import com.insideout.usecase.memoryMarble.GetMemoryMarblesByPaginationUseCase
import com.insideout.v1.endpoint.objectField.MemoryMarbleHttpResponse
import com.insideout.v1.endpoint.paginationField.PaginationHttpResponse
import com.insideout.v1.endpoint.paginationField.PaginationRequestParam
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GetMemoryMarblesByPaginationRestController(
    private val getMemoryMarblesByPaginationUseCase: GetMemoryMarblesByPaginationUseCase,
) {
    @GetMapping(
        value = ["/api/v1/memoryMarbles"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun getMemoryMarblesByPagination(
        @RequestHeader memberId: Long,
        @RequestParam storeType: StoreType? = null,
        @ModelAttribute @Valid paginationRequestParam: PaginationRequestParam,
    ): MemoryMarblesPaginationHttpResponse {
        return getMemoryMarblesByPaginationUseCase.execute(
            GetMemoryMarblesByPaginationUseCase.Query(
                memberId = memberId,
                storeType = storeType,
                limitSearch =
                    with(paginationRequestParam) {
                        GetMemoryMarblesByPaginationUseCase.Query.LimitSearch(
                            size = size,
                            lastId = lastId,
                        )
                    },
            ),
        ).let(MemoryMarblesPaginationHttpResponse.Companion::from)
    }

    data class MemoryMarblesPaginationHttpResponse(
        override val hasNext: Boolean,
        override val content: List<MemoryMarbleHttpResponse>,
    ) : PaginationHttpResponse() {
        companion object {
            @JvmStatic
            fun from(pagination: Pagination<MemoryMarble>): MemoryMarblesPaginationHttpResponse {
                return with(pagination) {
                    MemoryMarblesPaginationHttpResponse(
                        hasNext = hasNext,
                        content = content.map(MemoryMarbleHttpResponse::from),
                    )
                }
            }
        }
    }
}

package com.insideout.v1.endpoint.memoryMarble.get

import com.insideout.usecase.memoryMarble.GetMemoryMarbleByIdUseCase
import com.insideout.v1.endpoint.objectField.MemoryMarbleHttpResponse
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class GetMemoryMarbleByIdRestController(
    private val getMemoryMarbleByIdUseCase: GetMemoryMarbleByIdUseCase,
) {
    @GetMapping(
        value = ["/api/v1/memoryMarbles/{memoryMarbleId}"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun getMemoryMarbleById(
        @RequestHeader memberId: Long,
        @PathVariable("memoryMarbleId") memoryMarbleId: Long,
    ): MemoryMarbleHttpResponse {
        return getMemoryMarbleByIdUseCase.execute(GetMemoryMarbleByIdUseCase.Query(memoryMarbleId))
            .let(MemoryMarbleHttpResponse::from)
    }
}

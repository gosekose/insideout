package com.insideout.v1.endpoint.memory.put

import com.insideout.model.memory.type.StoreType
import com.insideout.usecase.memory.UpdateMemoryMarbleStoreTypeUseCase
import com.insideout.v1.endpoint.objectField.MemoryMarbleHttpResponse
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class UpdateMemoryMarbleStoreTypeRestController(
    private val updateMemoryMarbleStoreTypeUseCase: UpdateMemoryMarbleStoreTypeUseCase,
) {
    @PutMapping(
        value = ["/api/v1/memoryMarbles/{memoryMarbleId}/storeTypes"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun updateMemoryMarbleStoreType(
        @RequestHeader memberId: Long,
        @PathVariable("memoryMarbleId") memoryMarbleId: Long,
        @RequestBody request: HttpRequest,
    ): MemoryMarbleHttpResponse {
        return updateMemoryMarbleStoreTypeUseCase.execute(
            request.toCommand(memoryMarbleId),
        ).let(MemoryMarbleHttpResponse::from)
    }

    data class HttpRequest(
        val storeType: StoreType,
    ) {
        fun toCommand(id: Long): UpdateMemoryMarbleStoreTypeUseCase.Command {
            return UpdateMemoryMarbleStoreTypeUseCase.Command(
                id = id,
                storeType = storeType,
            )
        }
    }
}

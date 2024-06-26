package com.insideout.v1.endpoint.memoryMarble.delete

import com.insideout.usecase.memoryMarble.RemoveMemoryMarbleUseCase
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class RemoveMemoryMarbleRestController(
    private val removeMemoryMarbleUseCase: RemoveMemoryMarbleUseCase,
) {
    @DeleteMapping(value = ["/api/v1/memoryMarbles/{memoryMarbleId}"])
    fun removeMemoryMarble(
        @RequestHeader memberId: Long,
        @PathVariable("memoryMarbleId") memoryMarbleId: Long,
    ) {
        removeMemoryMarbleUseCase.execute(RemoveMemoryMarbleUseCase.Command(memoryMarbleId))
    }
}

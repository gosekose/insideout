package com.insideout.v1.memory

import com.insideout.aggregate.CreateFeelingAndMemoryMarbleAggregate
import com.insideout.model.feeling.type.FeelingType
import com.insideout.model.memory.model.MemoryMarbleContent
import com.insideout.usecase.feeling.CreateFeelingsUseCase
import com.insideout.v1.objectField.MemoryMarbleHttpResponse
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class CreateMemoryMarbleRestController(
    private val createFeelingAndMemoryMarbleAggregate: CreateFeelingAndMemoryMarbleAggregate,
) {
    @PostMapping(
        value = ["/api/v1/memoryMarbles"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun createMemoryMarble(
        @RequestHeader memberId: Long,
        @RequestBody request: HttpRequest,
    ): MemoryMarbleHttpResponse {
        return createFeelingAndMemoryMarbleAggregate.create(request.toAggregateDefinition(memberId))
            .let(MemoryMarbleHttpResponse::from)
    }

    data class HttpRequest(
        val content: String,
        val feelings: List<FeelingDefinitionHttpRequest>,
    ) {
        fun toAggregateDefinition(memberId: Long): CreateFeelingAndMemoryMarbleAggregate.Definition {
            return CreateFeelingAndMemoryMarbleAggregate.Definition(
                memberId = memberId,
                feelingDefinitions = feelings.map { it.toFeelingDefinition() },
                memoryMarbleContent = MemoryMarbleContent(content),
            )
        }

        data class FeelingDefinitionHttpRequest(
            val score: Long,
            val type: FeelingType,
        ) {
            fun toFeelingDefinition(): CreateFeelingsUseCase.Definition.FeelingDefinition {
                return CreateFeelingsUseCase.Definition.FeelingDefinition(
                    score = score,
                    type = type,
                )
            }
        }
    }
}

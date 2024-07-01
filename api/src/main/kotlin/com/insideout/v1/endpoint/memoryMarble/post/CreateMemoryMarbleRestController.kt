package com.insideout.v1.endpoint.memoryMarble.post

import com.insideout.aggregate.CreateFeelingAndMemoryMarbleAggregate
import com.insideout.aggregate.CreateFeelingAndMemoryMarbleAggregate.Definition
import com.insideout.model.feeling.type.FeelingType
import com.insideout.model.memoryMarble.model.MemoryMarbleContent
import com.insideout.usecase.feeling.CreateFeelingsUseCase
import com.insideout.v1.endpoint.objectField.MemoryMarbleHttpResponse
import com.insideout.v1.endpoint.requestField.MemoryMarbleContentField
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
    )
    fun createMemoryMarble(
        @RequestHeader memberId: Long,
        @RequestBody request: HttpRequest,
    ): MemoryMarbleHttpResponse {
        return createFeelingAndMemoryMarbleAggregate.create(request.toAggregateDefinition(memberId))
            .let(MemoryMarbleHttpResponse::from)
    }

    data class HttpRequest(
        val content: MemoryMarbleContentField,
        val feelings: List<FeelingDefinitionHttpField>,
    ) {
        fun toAggregateDefinition(memberId: Long): Definition {
            return Definition(
                memberId = memberId,
                feelingDefinitions = feelings.map { it.toFeelingDefinition() },
                memoryMarbleContent =
                    MemoryMarbleContent(
                        description = content.description,
                        fileContents = content.toFileDefinition(),
                    ),
            )
        }

        data class FeelingDefinitionHttpField(
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

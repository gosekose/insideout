package com.insideout.v1.endpoint.memoryMarble.put

import com.insideout.aggregate.RedefineFeelingAndMemoryMarbleAggregate
import com.insideout.model.feeling.type.FeelingType
import com.insideout.model.memoryMarble.model.MemoryMarbleContent
import com.insideout.usecase.feeling.CreateFeelingsUseCase
import com.insideout.usecase.feeling.RedefineFeelingsUseCase
import com.insideout.v1.endpoint.objectField.MemoryMarbleHttpResponse
import com.insideout.v1.endpoint.requestField.MemoryMarbleContentField
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class RedefineMemoryMarbleRestController(
    private val redefineFeelingAndMemoryMarbleAggregate: RedefineFeelingAndMemoryMarbleAggregate,
) {
    @PutMapping(
        value = ["/api/v1/memoryMarbles/{memoryMarbleId}"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun redefineMemoryMarble(
        @RequestHeader memberId: Long,
        @PathVariable("memoryMarbleId") memoryMarbleId: Long,
        @RequestBody request: HttpRequest,
    ): MemoryMarbleHttpResponse {
        return redefineFeelingAndMemoryMarbleAggregate.execute(request.toMemoryMarbleRedefinition(memoryMarbleId))
            .let(MemoryMarbleHttpResponse::from)
    }

    data class HttpRequest(
        val feelingsDefinition: List<FeelingDefinitionHttpField>,
        val feelingsRedefinition: List<FeelingRedefinitionHttpField>,
        val content: MemoryMarbleContentField,
    ) {
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

        data class FeelingRedefinitionHttpField(
            val id: Long,
            val score: Long,
            val type: FeelingType,
        ) {
            fun toFeelingRedefinition(): RedefineFeelingsUseCase.Redefinition.FeelingDefinition {
                return RedefineFeelingsUseCase.Redefinition.FeelingDefinition(
                    id = id,
                    score = score,
                    type = type,
                )
            }
        }

        fun toMemoryMarbleRedefinition(id: Long): RedefineFeelingAndMemoryMarbleAggregate.Redefinition {
            return RedefineFeelingAndMemoryMarbleAggregate.Redefinition(
                id = id,
                feelingDefinitions = feelingsDefinition.map { it.toFeelingDefinition() },
                feelingRedefinitions = feelingsRedefinition.map { it.toFeelingRedefinition() },
                MemoryMarbleContent(
                    description = content.description,
                    fileContents = content.toFileDefinition(),
                ),
            )
        }
    }
}

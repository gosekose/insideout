package com.insideout.aggregate

import com.insideout.model.memoryMarble.MemoryMarble
import com.insideout.model.memoryMarble.model.MemoryMarbleContent
import com.insideout.usecase.feeling.CreateFeelingsUseCase

interface CreateFeelingAndMemoryMarbleAggregate {
    fun create(definition: Definition): MemoryMarble

    data class Definition(
        val memberId: Long,
        val feelingDefinitions: List<CreateFeelingsUseCase.Definition.FeelingDefinition>,
        val memoryMarbleContent: MemoryMarbleContent,
    )
}

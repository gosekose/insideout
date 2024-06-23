package com.insideout.aggregate

import com.insideout.model.memory.MemoryMarble
import com.insideout.model.memory.model.Content
import com.insideout.usecase.feeling.CreateFeelingsUseCase
import com.insideout.usecase.feeling.RedefineFeelingsUseCase

interface RedefineFeelingAndMemoryMarbleAggregate {
    fun execute(redefinition: Redefinition): MemoryMarble

    data class Redefinition(
        val id: Long,
        val feelingDefinitions: List<CreateFeelingsUseCase.Definition.FeelingDefinition>,
        val feelingRedefinitions: List<RedefineFeelingsUseCase.Redefinition.FeelingDefinition>,
        val content: Content,
    )
}

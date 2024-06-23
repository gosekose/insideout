package com.insideout.usecase.feeling

import com.insideout.model.feeling.Feelings
import com.insideout.model.memory.MemoryMarble

interface FeelingsOperatorUseCase {
    fun operate(command: Command): Feelings

    data class Command(
        val memoryMarble: MemoryMarble,
        val feelingDefinitions: List<CreateFeelingsUseCase.Definition.FeelingDefinition>,
        val feelingRedefinitions: List<RedefineFeelingsUseCase.Redefinition.FeelingDefinition>,
    )
}

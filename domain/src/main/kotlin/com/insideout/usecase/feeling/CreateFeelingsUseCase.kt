package com.insideout.usecase.feeling

import com.insideout.model.feeling.Feelings
import com.insideout.model.feeling.type.FeelingType

interface CreateFeelingsUseCase {
    fun execute(definition: Definition): Feelings

    data class Definition(
        val memberId: Long,
        val feelingDefinitions: List<FeelingDefinition>,
    ) {
        data class FeelingDefinition(
            val score: Long,
            val type: FeelingType,
        )
    }
}
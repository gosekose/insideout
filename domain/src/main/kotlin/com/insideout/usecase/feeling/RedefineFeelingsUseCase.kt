package com.insideout.usecase.feeling

import com.insideout.model.feeling.Feelings
import com.insideout.model.feeling.type.FeelingType

interface RedefineFeelingsUseCase {
    fun execute(redefinition: Redefinition): Feelings

    data class Redefinition(
        val feelingDefinitions: List<FeelingDefinition>,
    ) {
        data class FeelingDefinition(
            val id: Long,
            val score: Long,
            val type: FeelingType,
        )
    }
}

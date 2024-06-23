package com.insideout.usecase.feeling

import com.insideout.model.feeling.Feelings

interface GetFeelingsUseCase {
    fun execute(query: Query): Feelings

    data class Query(
        val ids: List<Long>,
    )
}

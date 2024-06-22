package com.insideout.usecase.feeling

import com.insideout.model.feeling.Feelings

interface RemoveFeelingsUseCase {
    fun execute(command: Command)

    data class Command(
        val feelings: Feelings
    )
}
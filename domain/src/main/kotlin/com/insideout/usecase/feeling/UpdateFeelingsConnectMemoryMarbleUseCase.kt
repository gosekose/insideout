package com.insideout.usecase.feeling

import com.insideout.model.feeling.Feelings

interface UpdateFeelingsConnectMemoryMarbleUseCase {
    fun execute(command: Command): Feelings

    data class Command(
        val memoryMarbleId: Long,
        val feelings: Feelings,
    )
}

package com.insideout.usecase.memory

import com.insideout.model.feeling.Feelings
import com.insideout.model.memory.MemoryMarble
import com.insideout.model.memory.model.Content

interface UpdateMemoryMarbleUseCase {
    fun execute(command: Command): MemoryMarble

    data class Command(
        val id: Long,
        val feelings: Feelings,
        val content: Content,
    )
}

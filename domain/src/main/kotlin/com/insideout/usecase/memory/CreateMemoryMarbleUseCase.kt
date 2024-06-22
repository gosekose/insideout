package com.insideout.usecase.memory

import com.insideout.model.feeling.Feelings
import com.insideout.model.memory.MemoryMarble
import com.insideout.model.memory.model.Content

interface CreateMemoryMarbleUseCase {
    fun execute(definition: Definition): MemoryMarble

    data class Definition(
        val memberId: Long,
        val feelings: Feelings,
        val content: Content,
    )
}

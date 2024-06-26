package com.insideout.usecase.memoryMarble

import com.insideout.model.feeling.Feelings
import com.insideout.model.memoryMarble.MemoryMarble
import com.insideout.model.memoryMarble.model.MemoryMarbleContent

interface CreateMemoryMarbleUseCase {
    fun execute(definition: Definition): MemoryMarble

    data class Definition(
        val memberId: Long,
        val feelings: Feelings,
        val memoryMarbleContent: MemoryMarbleContent,
    )
}

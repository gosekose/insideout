package com.insideout.usecase.memory

import com.insideout.model.feeling.Feelings
import com.insideout.model.memory.MemoryMarble
import com.insideout.model.memory.model.MemoryMarbleContent

interface RedefineMemoryMarbleUseCase {
    fun execute(redefinition: Redefinition): MemoryMarble

    data class Redefinition(
        val memoryMarble: MemoryMarble,
        val feelings: Feelings,
        val memoryMarbleContent: MemoryMarbleContent,
    )
}

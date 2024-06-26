package com.insideout.usecase.memoryMarble

import com.insideout.model.feeling.Feelings
import com.insideout.model.memoryMarble.MemoryMarble
import com.insideout.model.memoryMarble.model.MemoryMarbleContent

interface RedefineMemoryMarbleUseCase {
    fun execute(redefinition: Redefinition): MemoryMarble

    data class Redefinition(
        val memoryMarble: MemoryMarble,
        val feelings: Feelings,
        val memoryMarbleContent: MemoryMarbleContent,
    )
}

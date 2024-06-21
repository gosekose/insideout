package com.insideout.usecase.memory.port

import com.insideout.usecase.memory.RemoveMemoryMarbleUseCase

interface MemoryMarbleRemover {
    fun remove(command: RemoveMemoryMarbleUseCase.Command)
}

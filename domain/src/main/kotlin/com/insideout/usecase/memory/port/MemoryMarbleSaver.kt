package com.insideout.usecase.memory.port

import com.insideout.model.memory.MemoryMarble
import com.insideout.usecase.memory.CreateMemoryMarbleUseCase

interface MemoryMarbleSaver {
    fun save(command: CreateMemoryMarbleUseCase.Command): MemoryMarble
}

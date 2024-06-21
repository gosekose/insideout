package com.insideout.usecase.memory.port

import com.insideout.model.memory.MemoryMarble
import com.insideout.usecase.memory.UpdateMemoryMarbleStoreTypeUseCase
import com.insideout.usecase.memory.UpdateMemoryMarbleUseCase

interface MemoryMarbleUpdater {
    fun update(command: UpdateMemoryMarbleUseCase.Command): MemoryMarble

    fun update(command: UpdateMemoryMarbleStoreTypeUseCase.Command): MemoryMarble
}

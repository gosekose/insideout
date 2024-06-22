package com.insideout.usecase.memory.port

import com.insideout.model.memory.MemoryMarble
import com.insideout.usecase.memory.UpdateMemoryMarbleStoreTypeUseCase

interface MemoryMarbleUpdater {
    fun update(command: UpdateMemoryMarbleStoreTypeUseCase.Command): MemoryMarble
}

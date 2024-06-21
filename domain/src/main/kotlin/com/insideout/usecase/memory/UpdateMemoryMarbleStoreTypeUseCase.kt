package com.insideout.usecase.memory

import com.insideout.model.memory.MemoryMarble
import com.insideout.model.memory.type.StoreType

interface UpdateMemoryMarbleStoreTypeUseCase {
    fun execute(command: Command): MemoryMarble

    data class Command(
        val id: Long,
        val storyType: StoreType,
    )
}

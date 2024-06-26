package com.insideout.usecase.memoryMarble

import com.insideout.model.memoryMarble.MemoryMarble
import com.insideout.model.memoryMarble.type.StoreType

interface UpdateMemoryMarbleStoreTypeUseCase {
    fun execute(command: Command): MemoryMarble

    data class Command(
        val id: Long,
        val storeType: StoreType,
    )
}

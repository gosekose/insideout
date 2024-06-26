package com.insideout.usecase.memoryMarble

interface RemoveMemoryMarbleUseCase {
    fun execute(command: Command)

    data class Command(
        val id: Long,
    )
}

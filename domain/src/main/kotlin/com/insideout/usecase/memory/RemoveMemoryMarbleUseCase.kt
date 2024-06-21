package com.insideout.usecase.memory

interface RemoveMemoryMarbleUseCase {
    fun execute(command: Command)

    data class Command(
        val id: Long,
    )
}

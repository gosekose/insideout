package com.insideout.usecase.memory

import com.insideout.usecase.memory.port.MemoryMarbleRemover
import org.springframework.stereotype.Component

@Component
class RemoveMemoryMarbleService(
    private val memoryMarbleRemover: MemoryMarbleRemover,
) : RemoveMemoryMarbleUseCase {
    override fun execute(command: RemoveMemoryMarbleUseCase.Command) {
        memoryMarbleRemover.remove(command.id)
    }
}

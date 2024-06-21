package com.insideout.usecase.memory

import com.insideout.usecase.memory.port.MemoryMarbleRemover
import org.springframework.stereotype.Component

@Component
class RemoveMemoryMarbleService(
    private val removeMemoryMarbleRemover: MemoryMarbleRemover,
) : RemoveMemoryMarbleUseCase {
    override fun execute(command: RemoveMemoryMarbleUseCase.Command) {
        removeMemoryMarbleRemover.remove(RemoveMemoryMarbleUseCase.Command(command.id))
    }
}

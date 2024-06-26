package com.insideout.usecase.memoryMarble

import com.insideout.usecase.memoryMarble.port.MemoryMarbleRemover
import org.springframework.stereotype.Component

@Component
class RemoveMemoryMarbleService(
    private val memoryMarbleRemover: MemoryMarbleRemover,
) : RemoveMemoryMarbleUseCase {
    override fun execute(command: RemoveMemoryMarbleUseCase.Command) {
        memoryMarbleRemover.remove(command.id)
    }
}

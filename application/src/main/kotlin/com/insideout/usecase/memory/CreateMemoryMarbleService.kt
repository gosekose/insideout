package com.insideout.usecase.memory

import com.insideout.model.memory.MemoryMarble
import com.insideout.usecase.memory.port.MemoryMarbleSaver
import org.springframework.stereotype.Component

@Component
class CreateMemoryMarbleService(
    private val memoryMarbleSaver: MemoryMarbleSaver,
) : CreateMemoryMarbleUseCase {
    override fun execute(command: CreateMemoryMarbleUseCase.Command): MemoryMarble {
        return memoryMarbleSaver.save(command)
    }
}

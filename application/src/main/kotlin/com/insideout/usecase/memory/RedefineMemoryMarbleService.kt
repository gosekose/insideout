package com.insideout.usecase.memory

import com.insideout.model.memory.MemoryMarble
import com.insideout.usecase.memory.port.MemoryMarbleSaver
import org.springframework.stereotype.Service

@Service
class RedefineMemoryMarbleService(
    private val memoryMarbleSaver: MemoryMarbleSaver,
) : RedefineMemoryMarbleUseCase {
    override fun execute(redefinition: RedefineMemoryMarbleUseCase.Redefinition): MemoryMarble {
        val (memoryMarble, feelings, content) = redefinition
        return memoryMarbleSaver.save(
            memoryMarble.update(
                feelings = feelings,
                content = content,
            ),
        )
    }
}

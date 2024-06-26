package com.insideout.usecase.memoryMarble

import com.insideout.model.memoryMarble.MemoryMarble
import com.insideout.usecase.memoryMarble.port.MemoryMarbleSaver
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
                memoryMarbleContent = content,
            ),
        )
    }
}

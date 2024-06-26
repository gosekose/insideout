package com.insideout.usecase.memoryMarble

import com.insideout.model.memoryMarble.MemoryMarble
import com.insideout.model.memoryMarble.type.StoreType
import com.insideout.usecase.memoryMarble.port.MemoryMarbleSaver
import org.springframework.stereotype.Component

@Component
class CreateMemoryMarbleService(
    private val memoryMarbleSaver: MemoryMarbleSaver,
) : CreateMemoryMarbleUseCase {
    override fun execute(definition: CreateMemoryMarbleUseCase.Definition): MemoryMarble {
        val (memberId, feelings, content) = definition

        return memoryMarbleSaver.save(
            MemoryMarble(
                memberId = memberId,
                feelings = feelings,
                memoryMarbleContent = content,
                storeType = StoreType.DAILY,
            ),
        )
    }
}

package com.insideout.usecase.memory

import com.insideout.model.memory.MemoryMarble
import com.insideout.model.memory.type.StoreType
import com.insideout.usecase.memory.port.MemoryMarbleSaver
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
                content = content,
                storeType = StoreType.DAILY,
            ),
        )
    }
}

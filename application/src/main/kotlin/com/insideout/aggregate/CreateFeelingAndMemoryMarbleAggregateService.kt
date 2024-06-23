package com.insideout.aggregate

import com.insideout.model.memory.MemoryMarble
import com.insideout.usecase.feeling.CreateFeelingsUseCase
import com.insideout.usecase.feeling.UpdateFeelingsConnectMemoryMarbleUseCase
import com.insideout.usecase.memory.CreateMemoryMarbleUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = false)
class CreateFeelingAndMemoryMarbleAggregateService(
    private val createFeelingsUseCase: CreateFeelingsUseCase,
    private val createMemoryMarbleUseCase: CreateMemoryMarbleUseCase,
    private val updateFeelingsConnectMemoryMarbleUseCase: UpdateFeelingsConnectMemoryMarbleUseCase,
) : CreateFeelingAndMemoryMarbleAggregate {
    override fun create(definition: CreateFeelingAndMemoryMarbleAggregate.Definition): MemoryMarble {
        val (memberId, feelingDefinitions, content) = definition

        val feelings =
            createFeelingsUseCase.execute(
                CreateFeelingsUseCase.Definition(
                    memberId = memberId,
                    feelingDefinitions = feelingDefinitions,
                ),
            )

        val memoryMarble =
            createMemoryMarbleUseCase.execute(
                CreateMemoryMarbleUseCase.Definition(
                    memberId = memberId,
                    feelings = feelings,
                    content = content,
                ),
            )

        updateFeelingsConnectMemoryMarbleUseCase.execute(
            UpdateFeelingsConnectMemoryMarbleUseCase.Command(
                memoryMarbleId = memoryMarble.id,
                feelings = feelings,
            ),
        )

        return memoryMarble
    }
}

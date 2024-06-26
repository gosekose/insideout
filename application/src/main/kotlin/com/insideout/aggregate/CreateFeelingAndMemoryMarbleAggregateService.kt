package com.insideout.aggregate

import com.insideout.distributeLock.DistributedLockBeforeTransaction
import com.insideout.model.memoryMarble.MemoryMarble
import com.insideout.usecase.feeling.CreateFeelingsUseCase
import com.insideout.usecase.feeling.UpdateFeelingsConnectMemoryMarbleUseCase
import com.insideout.usecase.memoryMarble.CreateMemoryMarbleUseCase
import org.springframework.stereotype.Service

@Service
class CreateFeelingAndMemoryMarbleAggregateService(
    private val createFeelingsUseCase: CreateFeelingsUseCase,
    private val createMemoryMarbleUseCase: CreateMemoryMarbleUseCase,
    private val updateFeelingsConnectMemoryMarbleUseCase: UpdateFeelingsConnectMemoryMarbleUseCase,
) : CreateFeelingAndMemoryMarbleAggregate {
    @DistributedLockBeforeTransaction(
        key = ["#definition.memberId"],
        name = "",
        transactionalReadOnly = false,
    )
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
                    memoryMarbleContent = content,
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

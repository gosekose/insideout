package com.insideout.aggregate

import com.insideout.distributeLock.DistributedLockBeforeTransaction
import com.insideout.distributeLock.DistributedLockPrefixKey
import com.insideout.model.memoryMarble.MemoryMarble
import com.insideout.usecase.feeling.FeelingsOperatorUseCase
import com.insideout.usecase.memoryMarble.GetMemoryMarbleByIdUseCase
import com.insideout.usecase.memoryMarble.RedefineMemoryMarbleUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = false)
class RedefineFeelingAndMemoryMarbleAggregateService(
    private val feelingsOperatorUseCase: FeelingsOperatorUseCase,
    private val getMemoryMarbleByIdUseCase: GetMemoryMarbleByIdUseCase,
    private val redefineMemoryMarbleUseCase: RedefineMemoryMarbleUseCase,
) : RedefineFeelingAndMemoryMarbleAggregate {
    @DistributedLockBeforeTransaction(
        key = ["#redefinition.id"],
        prefix = DistributedLockPrefixKey.MEMORY_MARBLE_UPDATE_WITH_MEMORY_MARBLE_ID,
        transactionalReadOnly = false,
    )
    override fun execute(redefinition: RedefineFeelingAndMemoryMarbleAggregate.Redefinition): MemoryMarble {
        val (id, feelingDefinitions, feelingRedefinitions, content) = redefinition

        val memoryMarble =
            getMemoryMarbleByIdUseCase.execute(
                GetMemoryMarbleByIdUseCase.Query(
                    id = id,
                ),
            )

        val feelings =
            feelingsOperatorUseCase.operate(
                FeelingsOperatorUseCase.Command(
                    memoryMarble = memoryMarble,
                    feelingDefinitions = feelingDefinitions,
                    feelingRedefinitions = feelingRedefinitions,
                ),
            )

        return redefineMemoryMarbleUseCase.execute(
            RedefineMemoryMarbleUseCase.Redefinition(
                memoryMarble = memoryMarble,
                feelings = feelings,
                memoryMarbleContent = content,
            ),
        )
    }
}

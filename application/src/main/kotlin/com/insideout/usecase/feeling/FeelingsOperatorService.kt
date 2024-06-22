package com.insideout.usecase.feeling

import com.insideout.model.feeling.Feelings
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = false)
class FeelingsOperatorService(
    private val createFeelingsUseCase: CreateFeelingsUseCase,
    private val removeFeelingsUseCase: RemoveFeelingsUseCase,
    private val redefineFeelingsUseCase: RedefineFeelingsUseCase,
    private val updateFeelingsConnectMemoryMarbleUseCase: UpdateFeelingsConnectMemoryMarbleUseCase,
) : FeelingsOperatorUseCase {
    override fun operate(command: FeelingsOperatorUseCase.Command): Feelings {
        val (memoryMarble, feelingDefinitions, feelingRedefinitions) = command

        val connectedFeelings = createFeelingsUseCase.execute(
            CreateFeelingsUseCase.Definition(
                memberId = memoryMarble.memberId,
                feelingDefinitions = feelingDefinitions
            )
        ).let { createdFeelings ->
            updateFeelingsConnectMemoryMarbleUseCase.execute(
                UpdateFeelingsConnectMemoryMarbleUseCase.Command(
                    memoryMarbleId = memoryMarble.id,
                    feelings = createdFeelings
                )
            )
        }

        val redefinedFeelings = redefineFeelingsUseCase.execute(
            RedefineFeelingsUseCase.Redefinition(
                feelingDefinitions = feelingRedefinitions
            )
        )

        val toBeFeelings = (connectedFeelings + redefinedFeelings)
        val toBeFeelingsMap = toBeFeelings.associateBy { it.id }

        memoryMarble.feelings.filter { it.id !in toBeFeelingsMap }
            .let(::Feelings)
            .let { removeFeelingsUseCase.execute(RemoveFeelingsUseCase.Command(it)) }

        return toBeFeelings
    }
}
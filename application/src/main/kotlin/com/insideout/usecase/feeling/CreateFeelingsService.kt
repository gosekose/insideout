package com.insideout.usecase.feeling

import com.insideout.model.feeling.Feeling
import com.insideout.model.feeling.Feelings
import com.insideout.model.feeling.model.FeelingMemoryMarbleConnect
import com.insideout.usecase.feeling.port.FeelingSaver
import org.springframework.stereotype.Service

@Service
class CreateFeelingsService(
    private val feelingSaver: FeelingSaver,
) : CreateFeelingsUseCase {
    override fun execute(definition: CreateFeelingsUseCase.Definition): Feelings {
        val (memberId, feelingDefinitions) = definition

        val feelings =
            feelingDefinitions.map {
                Feeling(
                    memberId = memberId,
                    score = it.score,
                    type = it.type,
                    memoryMarbleConnect = FeelingMemoryMarbleConnect.DisConnectMemoryMarble,
                )
            }.let(::Feelings)

        return feelingSaver.saveAll(feelings)
    }
}

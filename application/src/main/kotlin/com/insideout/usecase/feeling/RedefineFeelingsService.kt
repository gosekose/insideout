package com.insideout.usecase.feeling

import com.insideout.model.feeling.Feelings
import com.insideout.usecase.feeling.port.FeelingReader
import com.insideout.usecase.feeling.port.FeelingSaver
import org.springframework.stereotype.Service

@Service
class RedefineFeelingsService(
    private val feelingSaver: FeelingSaver,
    private val feelingReader: FeelingReader,
) : RedefineFeelingsUseCase {
    override fun execute(redefinition: RedefineFeelingsUseCase.Redefinition): Feelings {
        val feelingRedefinition = redefinition.feelingDefinitions.associateBy { it.id }
        val asIsFeelings = feelingReader.getByIds(feelingRedefinition.map { it.key }.toList())

        val toBeFeelings =
            asIsFeelings.mapNotNull { feeling ->
                feelingRedefinition[feeling.id]?.let { definition ->
                    feeling.update(definition.score, definition.type)
                }
            }.let(::Feelings)

        return feelingSaver.saveAll(toBeFeelings)
    }
}

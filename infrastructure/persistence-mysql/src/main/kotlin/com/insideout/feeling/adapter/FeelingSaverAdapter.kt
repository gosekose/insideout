package com.insideout.feeling.adapter

import com.insideout.annotation.WritableTransactional
import com.insideout.feeling.model.FeelingJpaEntity
import com.insideout.feeling.repository.FeelingJpaRepository
import com.insideout.model.feeling.Feelings
import com.insideout.usecase.feeling.port.FeelingSaver
import org.springframework.stereotype.Component

@Component
@WritableTransactional
class FeelingSaverAdapter(
    private val feelingJpaRepository: FeelingJpaRepository,
) : FeelingSaver {
    override fun saveAll(feelings: Feelings): Feelings {
        return feelings.map { FeelingJpaEntity.from(it) }
            .let { feelingJpaRepository.saveAll(it) }
            .map { it.toModel() }
            .let(::Feelings)
    }
}

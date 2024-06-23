package com.insideout.feeling.adapter

import com.insideout.annotation.WritableTransactional
import com.insideout.feeling.model.FeelingJpaEntity
import com.insideout.feeling.repository.FeelingJpaRepository
import com.insideout.model.feeling.Feelings
import com.insideout.usecase.feeling.port.FeelingSaver
import org.springframework.stereotype.Repository

@Repository
@WritableTransactional
class FeelingSaverAdapter(
    private val feelingJpaRepository: FeelingJpaRepository,
) : FeelingSaver {
    override fun saveAll(feelings: Feelings): Feelings {
        return feelingJpaRepository.saveAll(feelings.map(FeelingJpaEntity::from))
            .map { it.toModel() }
            .let(::Feelings)
    }
}

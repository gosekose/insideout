package com.insideout.feeling.adapter

import com.insideout.annotation.WritableTransactional
import com.insideout.feeling.model.FeelingJpaEntity
import com.insideout.feeling.repository.FeelingJpaRepository
import com.insideout.model.feeling.Feelings
import com.insideout.usecase.feeling.port.FeelingRemover
import org.springframework.stereotype.Repository

@Repository
@WritableTransactional
class FeelingRemoverAdapter(
    private val feelingJpaRepository: FeelingJpaRepository,
) : FeelingRemover {
    override fun remove(feelings: Feelings) {
        feelingJpaRepository.saveAll(feelings.map(FeelingJpaEntity::from).map { it.remove() })
    }
}

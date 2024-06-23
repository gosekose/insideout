package com.insideout.feeling.adapter

import com.insideout.annotation.ReadOnlyTransactional
import com.insideout.feeling.repository.FeelingJpaRepository
import com.insideout.model.feeling.Feelings
import com.insideout.usecase.feeling.port.FeelingReader
import org.springframework.stereotype.Repository

@Repository
@ReadOnlyTransactional
class FeelingReaderAdapter(
    private val feelingJpaRepository: FeelingJpaRepository,
) : FeelingReader {
    override fun getByIds(ids: List<Long>): Feelings {
        return feelingJpaRepository.findByIdInAndSoftDeleteStatus(ids)
            .map { it.toModel() }
            .let(::Feelings)
    }
}

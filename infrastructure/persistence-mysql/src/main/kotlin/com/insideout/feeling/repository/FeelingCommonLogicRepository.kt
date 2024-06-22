package com.insideout.feeling.repository

import com.insideout.model.feeling.Feelings
import org.springframework.stereotype.Repository

@Repository
interface FeelingCommonLogicRepository {
    fun findFeelings(feelingIds: List<Long>): Feelings
}

class FeelingCommandLogicRepositoryImpl(
    private val feelingJpaRepository: FeelingJpaRepository,
) : FeelingCommonLogicRepository {
    override fun findFeelings(feelingIds: List<Long>): Feelings {
        return feelingJpaRepository.findByIdInAndSoftDeleteStatus(feelingIds)
            .sortedBy { it.createdAt }
            .map { it.toModel() }
            .let(::Feelings)
    }
}
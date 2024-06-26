package com.insideout.memoryMarble.adapter

import com.insideout.annotation.ReadOnlyTransactional
import com.insideout.feeling.repository.FeelingJpaRepository
import com.insideout.memoryMarble.repository.MemoryMarbleJpaRepository
import com.insideout.model.feeling.Feelings
import com.insideout.model.memoryMarble.MemoryMarble
import com.insideout.model.memoryMarble.MemoryMarbles
import com.insideout.usecase.memoryMarble.GetMemoryMarblesByPaginationUseCase
import com.insideout.usecase.memoryMarble.port.MemoryMarbleReader
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
@ReadOnlyTransactional
class MemoryMarbleReaderAdapter(
    private val feelingJpaRepository: FeelingJpaRepository,
    private val memoryMarbleJpaRepository: MemoryMarbleJpaRepository,
) : MemoryMarbleReader {
    override fun getOrNull(id: Long): MemoryMarble? {
        return memoryMarbleJpaRepository.findByIdOrNull(id)?.let { it.toModel(getFeelings(it.feelingIds)) }
    }

    override fun getAll(query: GetMemoryMarblesByPaginationUseCase.Query): MemoryMarbles {
        val memoryMarbleJpaEntities =
            with(query) {
                memoryMarbleJpaRepository.findByLimitSearch(
                    memberId = memberId,
                    storeType = storeType,
                    lastId = limitSearch.lastId,
                    size = limitSearch.size,
                )
            }

        val feelingsMap =
            feelingJpaRepository.findByIdInAndSoftDeleteStatus(
                ids = memoryMarbleJpaEntities.flatMap { it.feelingIds },
            ).associateBy { it.id }

        return memoryMarbleJpaEntities.map { memoryMarble ->
            val findFeelings =
                memoryMarble.feelingIds
                    .mapNotNull { feelingsMap[it]?.toModel() }
                    .sortedBy { it.createdAt }
                    .let(::Feelings)
            memoryMarble.toModel(findFeelings)
        }.let(::MemoryMarbles)
    }

    override fun hasNext(currentId: Long): Boolean {
        return memoryMarbleJpaRepository.existsByIdGreaterThan(currentId)
    }

    private fun getFeelings(feelingIds: List<Long>): Feelings {
        return feelingJpaRepository.findByIdInAndSoftDeleteStatus(feelingIds)
            .sortedBy { it.createdAt }
            .map { it.toModel() }
            .let(::Feelings)
    }
}

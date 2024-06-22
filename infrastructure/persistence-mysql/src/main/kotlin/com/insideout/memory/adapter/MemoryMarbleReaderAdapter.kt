package com.insideout.memory.adapter

import com.insideout.annotation.ReadOnlyTransactional
import com.insideout.feeling.repository.FeelingJpaRepository
import com.insideout.memory.repository.MemoryMarbleJpaRepository
import com.insideout.model.feeling.Feelings
import com.insideout.model.memory.MemoryMarble
import com.insideout.model.memory.MemoryMarbles
import com.insideout.usecase.memory.GetMemoryMarbleUseCase
import com.insideout.usecase.memory.port.MemoryMarbleReader
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
@ReadOnlyTransactional
class MemoryMarbleReaderAdapter(
    private val feelingJpaRepository: FeelingJpaRepository,
    private val memoryMarbleJpaRepository: MemoryMarbleJpaRepository,
) : MemoryMarbleReader {
    override fun getOrNull(id: Long): MemoryMarble? {
        return memoryMarbleJpaRepository.findByIdOrNull(id)?.let { it.toModel(getFeelings(it.feelingIds)) }
    }

    override fun getAll(query: GetMemoryMarbleUseCase.Query): MemoryMarbles {
        val memoryMarbleJpaEntities =
            with(query) {
                memoryMarbleJpaRepository.findByOffsetSearch(
                    memberId = memberId,
                    storeType = storeType,
                    lastId = offsetSearch.lastId,
                    size = offsetSearch.size,
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

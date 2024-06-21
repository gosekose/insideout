package com.insideout.memory.adapter

import com.insideout.annotation.ReadOnlyTransactional
import com.insideout.feeling.repository.FeelingJpaRepository
import com.insideout.memory.repository.MemoryMarbleJpaRepository
import com.insideout.model.feeling.Feelings
import com.insideout.model.memory.MemoryMarble
import com.insideout.model.memory.MemoryMarbles
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

    override fun getAll(ids: List<Long>): MemoryMarbles {
        return memoryMarbleJpaRepository.findByIdInAndSoftDeleteStatus(ids)
            .map { it.toModel(getFeelings(it.feelingIds)) }
            .let(::MemoryMarbles)
    }

    private fun getFeelings(feelingsIds: List<Long>): Feelings {
        return if (feelingsIds.isEmpty()) {
            Feelings(mutableListOf())
        } else {
            feelingJpaRepository.findByIdInAndSoftDeleteStatus(feelingsIds)
                .asSequence()
                .sortedBy { it.createdAt }
                .map { it.toModel() }
                .toList()
                .let(::Feelings)
        }
    }
}

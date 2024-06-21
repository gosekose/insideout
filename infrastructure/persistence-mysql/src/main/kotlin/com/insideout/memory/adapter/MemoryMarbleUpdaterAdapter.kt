package com.insideout.memory.adapter

import com.insideout.annotation.WritableTransactional
import com.insideout.base.notnull
import com.insideout.feeling.model.FeelingJpaEntity
import com.insideout.feeling.repository.FeelingJpaRepository
import com.insideout.memory.model.field.ContentField
import com.insideout.memory.repository.MemoryMarbleJpaRepository
import com.insideout.model.feeling.Feelings
import com.insideout.model.memory.MemoryMarble
import com.insideout.usecase.memory.UpdateMemoryMarbleStoreTypeUseCase
import com.insideout.usecase.memory.UpdateMemoryMarbleUseCase
import com.insideout.usecase.memory.port.MemoryMarbleUpdater
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
@WritableTransactional
class MemoryMarbleUpdaterAdapter(
    private val feelingJpaRepository: FeelingJpaRepository,
    private val memoryMarbleJpaRepository: MemoryMarbleJpaRepository,
) : MemoryMarbleUpdater {
    override fun update(command: UpdateMemoryMarbleUseCase.Command): MemoryMarble {
        val (id, feelings, content) = command
        val memoryMarbleJpaEntity = memoryMarbleJpaRepository.findByIdOrNull(id).notnull()

        val asIsFeelingJpaEntitiesMap =
            feelingJpaRepository.findByIdInAndSoftDeleteStatus(memoryMarbleJpaEntity.feelingIds).associateBy { it.id }

        val toBeFeelingJpaEntities = feelings.map { FeelingJpaEntity.from(it) }

        removeFeelingsIfNotContains(asIsFeelingJpaEntitiesMap, toBeFeelingJpaEntities)

        val savedFeelings =
            feelingJpaRepository
                .saveAll(updateOrCreateFeelings(asIsFeelingJpaEntitiesMap, toBeFeelingJpaEntities))
                .map { it.toModel() }

        return memoryMarbleJpaRepository.save(
            memoryMarbleJpaEntity.update(
                feelings = savedFeelings.map { it.id },
                content = ContentField.from(content),
            ),
        ).toModel(Feelings(savedFeelings))
    }

    override fun update(command: UpdateMemoryMarbleStoreTypeUseCase.Command): MemoryMarble {
        val (id, storeType) = command
        val memoryMarble = memoryMarbleJpaRepository.findByIdOrNull(id).notnull()

        val feelings =
            feelingJpaRepository.findByIdInAndSoftDeleteStatus(memoryMarble.feelingIds)
                .map { it.toModel() }
        return memoryMarble.update(storeType).toModel(Feelings(feelings))
    }

    fun removeFeelingsIfNotContains(
        asIsFeelingJpaEntityMap: Map<Long, FeelingJpaEntity>,
        toBeFeelingJpaEntities: List<FeelingJpaEntity>,
    ) {
        toBeFeelingJpaEntities.asSequence()
            .filterNot { asIsFeelingJpaEntityMap.containsKey(it.id) }
            .forEach {
                feelingJpaRepository.save(it.remove())
            }
    }

    fun updateOrCreateFeelings(
        asIsFeelingJpaEntityMap: Map<Long, FeelingJpaEntity>,
        toBeFeelingJpaEntities: List<FeelingJpaEntity>,
    ): List<FeelingJpaEntity> {
        return toBeFeelingJpaEntities.map { toBe ->
            val asIs = asIsFeelingJpaEntityMap[toBe.id]
            asIs?.update(
                score = toBe.score,
                type = toBe.type,
            ) ?: toBe
        }
    }
}

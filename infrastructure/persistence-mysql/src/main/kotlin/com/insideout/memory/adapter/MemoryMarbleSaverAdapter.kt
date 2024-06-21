package com.insideout.memory.adapter

import com.insideout.annotation.WritableTransactional
import com.insideout.base.SoftDeleteStatus
import com.insideout.feeling.model.FeelingJpaEntity
import com.insideout.feeling.repository.FeelingJpaRepository
import com.insideout.memory.model.MemoryMarbleJpaEntity
import com.insideout.memory.model.field.ContentField
import com.insideout.memory.repository.MemoryMarbleJpaRepository
import com.insideout.model.memory.MemoryMarble
import com.insideout.model.memory.type.StoreType
import com.insideout.usecase.memory.CreateMemoryMarbleUseCase
import com.insideout.usecase.memory.port.MemoryMarbleSaver
import org.springframework.stereotype.Component

@Component
@WritableTransactional
class MemoryMarbleSaverAdapter(
    private val feelingJpaRepository: FeelingJpaRepository,
    private val memoryMarbleJpaRepository: MemoryMarbleJpaRepository,
) : MemoryMarbleSaver {
    override fun save(command: CreateMemoryMarbleUseCase.Command): MemoryMarble {
        val (memberId, feelings, content) = command
        val savedFeelings =
            feelingJpaRepository.saveAll(feelings.map { FeelingJpaEntity.from(it) })
                .map { it.toModel() }

        return memoryMarbleJpaRepository.save(
            MemoryMarbleJpaEntity(
                memberId = memberId,
                feelingIds = savedFeelings.map { it.id },
                content = ContentField.from(content),
                storeType = StoreType.DAILY,
                softDeleteStatus = SoftDeleteStatus.ACTIVE,
            ),
        ).toModel(feelings)
    }
}

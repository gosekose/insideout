package com.insideout.memory.model

import com.insideout.base.BaseJpaEntity
import com.insideout.base.SoftDeleteStatus
import com.insideout.base.applyWithDomainModel
import com.insideout.base.applyWithEntity
import com.insideout.converter.ListLongToStringConverter
import com.insideout.memory.model.model.MemoryMarbleContentJpaModel
import com.insideout.model.feeling.Feelings
import com.insideout.model.memory.MemoryMarble
import com.insideout.model.memory.type.StoreType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Index
import jakarta.persistence.Table

@Entity
@Table(
    name = "memory_marble",
    indexes = [
        Index(name = "idx_memory_marble__createdAt", columnList = "createdAt"),
        Index(name = "idx_memory_marble__memberId_storeType", columnList = "memberId, storeType"),
    ],
)
class MemoryMarbleJpaEntity(
    @Column(name = "memberId", columnDefinition = "bigint", nullable = false)
    val memberId: Long,
    @Embedded
    val content: MemoryMarbleContentJpaModel,
    @Column(name = "feelingIds", columnDefinition = "varchar(255)", nullable = false)
    @Convert(converter = ListLongToStringConverter::class)
    val feelingIds: List<Long> = mutableListOf(),
    @Enumerated(value = EnumType.STRING)
    @Column(name = "storeType", columnDefinition = "varchar(32)", nullable = false)
    val storeType: StoreType,
    @Enumerated
    @Column(name = "status", columnDefinition = "varchar(32)", nullable = false)
    var softDeleteStatus: SoftDeleteStatus,
) : BaseJpaEntity() {
    fun toModel(feelings: Feelings): MemoryMarble {
        return MemoryMarble(
            id = id,
            memberId = memberId,
            feelings = feelings,
            memoryMarbleContent = content.toModel(),
            storeType = storeType,
        ).applyWithEntity(this)
    }

    fun remove(): MemoryMarbleJpaEntity {
        return this.apply {
            this.softDeleteStatus = SoftDeleteStatus.INACTIVE
        }
    }

    companion object {
        @JvmStatic
        fun from(memoryMarble: MemoryMarble): MemoryMarbleJpaEntity {
            return with(memoryMarble) {
                MemoryMarbleJpaEntity(
                    memberId = memberId,
                    content = MemoryMarbleContentJpaModel.from(memoryMarbleContent),
                    feelingIds = feelings.map { it.id },
                    storeType = storeType,
                    softDeleteStatus = SoftDeleteStatus.ACTIVE,
                )
            }.applyWithDomainModel(memoryMarble)
        }
    }
}

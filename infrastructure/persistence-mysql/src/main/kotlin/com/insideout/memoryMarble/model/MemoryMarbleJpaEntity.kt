package com.insideout.memoryMarble.model

import com.insideout.base.BaseJpaEntity
import com.insideout.base.SoftDeleteStatus
import com.insideout.base.applyWithDomainModel
import com.insideout.base.applyWithEntity
import com.insideout.converter.ListLongToStringConverter
import com.insideout.memoryMarble.model.model.MemoryMarbleContentJpaModel
import com.insideout.model.feeling.Feelings
import com.insideout.model.memoryMarble.MemoryMarble
import com.insideout.model.memoryMarble.type.StoreType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table

@Entity
@Table(
    name = "memory_marbles",
    indexes = [
        Index(name = "idx_memory_marbles__created_at", columnList = "created_at"),
        Index(name = "idx_memory_marbles__member_id_store_type", columnList = "member_id, store_type"),
    ],
)
class MemoryMarbleJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column(name = "member_id", columnDefinition = "bigint", nullable = false)
    val memberId: Long,
    @Embedded
    val content: MemoryMarbleContentJpaModel,
    @Column(name = "feeling_ids", columnDefinition = "varchar(255)", nullable = false)
    @Convert(converter = ListLongToStringConverter::class)
    val feelingIds: List<Long> = mutableListOf(),
    @Enumerated(value = EnumType.STRING)
    @Column(name = "store_type", columnDefinition = "varchar(32)", nullable = false)
    val storeType: StoreType,
    @Enumerated(value = EnumType.STRING)
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
                    id = id,
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

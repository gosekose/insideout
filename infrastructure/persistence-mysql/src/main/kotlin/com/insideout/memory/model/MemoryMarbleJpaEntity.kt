package com.insideout.memory.model

import com.insideout.base.BaseJpaEntity
import com.insideout.base.SoftDeleteStatus
import com.insideout.base.applyWithEntity
import com.insideout.converter.ListLongToStringConverter
import com.insideout.memory.model.field.ContentField
import com.insideout.model.feeling.Feelings
import com.insideout.model.memory.MemoryMarble
import com.insideout.model.memory.type.StoreType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Embedded
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Table(name = "memory_marble")
class MemoryMarbleJpaEntity(
    @Column(name = "memberId", columnDefinition = "bigint", nullable = false)
    val memberId: Long,
    @Column(name = "feelingIds", columnDefinition = "varchar(255)", nullable = false)
    @Convert(converter = ListLongToStringConverter::class)
    var feelingIds: List<Long> = mutableListOf(),
    @Embedded
    var content: ContentField,
    @Enumerated(value = EnumType.STRING)
    @Column(name = "storeType", columnDefinition = "varchar(32)", nullable = false)
    var storeType: StoreType,
    @Enumerated
    @Column(name = "status", columnDefinition = "varchar(32)", nullable = false)
    var softDeleteStatus: SoftDeleteStatus,
) : BaseJpaEntity() {
    fun toModel(feelings: Feelings): MemoryMarble {
        return MemoryMarble(
            id = id,
            memberId = memberId,
            feelings = feelings,
            content = content.toModel(),
            storeType = storeType,
        ).applyWithEntity(this)
    }

    fun remove(): MemoryMarbleJpaEntity {
        return this.apply {
            softDeleteStatus = SoftDeleteStatus.INACTIVE
        }
    }

    fun update(
        feelings: List<Long>,
        content: ContentField,
    ): MemoryMarbleJpaEntity =
        this.apply {
            this.feelingIds = feelings
            this.content = content
        }

    fun update(storeType: StoreType) =
        this.apply {
            this.storeType = storeType
        }
}

package com.insideout.feeling.model

import com.insideout.base.BaseJpaEntity
import com.insideout.base.SoftDeleteStatus
import com.insideout.base.applyWithDomainModel
import com.insideout.base.applyWithEntity
import com.insideout.feeling.model.model.FeelingMemoryMarbleConnectJpaModel
import com.insideout.model.feeling.Feeling
import com.insideout.model.feeling.model.FeelingMemoryMarbleConnect.ConnectMemoryMarble
import com.insideout.model.feeling.model.FeelingMemoryMarbleConnect.DisConnectMemoryMarble
import com.insideout.model.feeling.model.FeelingMemoryMarbleConnect.MemoryMarbleConnectStatus
import com.insideout.model.feeling.type.FeelingType
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Entity
@Table(name = "feeling")
class FeelingJpaEntity(
    @Column(name = "memberId", columnDefinition = "bigint", nullable = false)
    val memberId: Long,
    @Column(name = "score", columnDefinition = "bigint", nullable = false)
    var score: Long,
    @Enumerated(value = EnumType.STRING)
    @Column(name = "type", columnDefinition = "varchar(64)", nullable = false)
    var type: FeelingType,
    @Enumerated
    @Column(name = "status", columnDefinition = "varchar(32)", nullable = false)
    var softDeleteStatus: SoftDeleteStatus,
    @Embedded
    val memoryMarbleConnect: FeelingMemoryMarbleConnectJpaModel,
) : BaseJpaEntity() {
    fun toModel(): Feeling {
        val memoryMarbleConnect =
            with(memoryMarbleConnect) {
                when (memoryMarbleConnectStatus) {
                    MemoryMarbleConnectStatus.DISCONNECT -> DisConnectMemoryMarble
                    MemoryMarbleConnectStatus.CONNECT ->
                        if (memoryMarbleId == null) {
                            DisConnectMemoryMarble
                        } else {
                            ConnectMemoryMarble(
                                memoryMarbleId = memoryMarbleId,
                            )
                        }
                }
            }

        return Feeling(
            id = id,
            memberId = memberId,
            score = score,
            type = type,
            memoryMarbleConnect = memoryMarbleConnect,
        ).applyWithEntity(this)
    }

    fun remove(): FeelingJpaEntity {
        return this.apply {
            this.softDeleteStatus = SoftDeleteStatus.INACTIVE
        }
    }

    fun update(
        score: Long,
        type: FeelingType,
    ): FeelingJpaEntity {
        return this.apply {
            this.score = score
            this.type = type
        }
    }

    companion object {
        @JvmStatic
        fun from(feeling: Feeling): FeelingJpaEntity {
            val memoryMarbleConnect =
                when (val connect = feeling.memoryMarbleConnect) {
                    is DisConnectMemoryMarble ->
                        FeelingMemoryMarbleConnectJpaModel(
                            memoryMarbleId = null,
                            memoryMarbleConnectStatus = MemoryMarbleConnectStatus.DISCONNECT,
                        )

                    is ConnectMemoryMarble ->
                        FeelingMemoryMarbleConnectJpaModel(
                            memoryMarbleId = connect.memoryMarbleId,
                            memoryMarbleConnectStatus = MemoryMarbleConnectStatus.CONNECT,
                        )
                }

            return with(feeling) {
                FeelingJpaEntity(
                    memberId = memberId,
                    score = score,
                    type = type,
                    softDeleteStatus = SoftDeleteStatus.ACTIVE,
                    memoryMarbleConnect = memoryMarbleConnect,
                ).applyWithDomainModel(this)
            }
        }
    }
}

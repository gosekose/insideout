package com.insideout.feeling.model.model

import com.insideout.model.feeling.model.FeelingMemoryMarbleConnect
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class FeelingMemoryMarbleConnectJpaModel(
    @Column(name = "memoryMarbleId", columnDefinition = "bigint", nullable = true)
    val memoryMarbleId: Long?,
    @Column(name = "memoryMarbleConnectStatus", columnDefinition = "varchar(16)", nullable = false)
    val memoryMarbleConnectStatus: FeelingMemoryMarbleConnect.MemoryMarbleConnectStatus,
)

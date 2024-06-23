package com.insideout.feeling.model.model

import com.insideout.model.feeling.model.FeelingMemoryMarbleConnect
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
data class FeelingMemoryMarbleConnectJpaModel(
    @Column(name = "memoryMarbleId", columnDefinition = "bigint", nullable = true)
    val memoryMarbleId: Long?,
    @Enumerated(value = EnumType.STRING)
    @Column(name = "memoryMarbleConnectStatus", columnDefinition = "varchar(32)", nullable = false)
    val memoryMarbleConnectStatus: FeelingMemoryMarbleConnect.MemoryMarbleConnectStatus,
)

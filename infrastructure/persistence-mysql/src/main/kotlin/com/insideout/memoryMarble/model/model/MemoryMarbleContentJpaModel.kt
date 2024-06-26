package com.insideout.memoryMarble.model.model

import com.insideout.model.memoryMarble.model.MemoryMarbleContent
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class MemoryMarbleContentJpaModel(
    @Column(name = "description", columnDefinition = "text", nullable = true)
    val description: String?,
) {
    fun toModel(): MemoryMarbleContent {
        return MemoryMarbleContent(
            description = description,
        )
    }

    companion object {
        @JvmStatic
        fun from(memoryMarbleContent: MemoryMarbleContent): MemoryMarbleContentJpaModel {
            return with(memoryMarbleContent) {
                MemoryMarbleContentJpaModel(
                    description = description,
                )
            }
        }
    }
}

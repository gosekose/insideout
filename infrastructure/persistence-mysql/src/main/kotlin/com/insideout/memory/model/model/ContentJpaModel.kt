package com.insideout.memory.model.model

import com.insideout.model.memory.model.Content
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class ContentJpaModel(
    @Column(name = "description", columnDefinition = "text", nullable = true)
    val description: String?,
) {
    fun toModel(): Content {
        return Content(
            description = description,
        )
    }

    companion object {
        @JvmStatic
        fun from(content: Content): ContentJpaModel {
            return with(content) {
                ContentJpaModel(
                    description = description,
                )
            }
        }
    }
}

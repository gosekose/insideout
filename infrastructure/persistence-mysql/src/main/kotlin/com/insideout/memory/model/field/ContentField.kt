package com.insideout.memory.model.field

import com.insideout.model.memory.model.Content
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class ContentField(
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
        fun from(content: Content): ContentField {
            return with(content) {
                ContentField(
                    description = description,
                )
            }
        }
    }
}

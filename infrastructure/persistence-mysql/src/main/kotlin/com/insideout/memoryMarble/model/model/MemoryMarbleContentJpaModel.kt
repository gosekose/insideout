package com.insideout.memoryMarble.model.model

import com.insideout.converter.ListFileContentToStringConverter
import com.insideout.model.memoryMarble.model.MemoryMarbleContent
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Embeddable

@Embeddable
data class MemoryMarbleContentJpaModel(
    @Column(name = "description", columnDefinition = "text", nullable = true)
    val description: String?,
    @Column(name = "file_content", columnDefinition = "text", nullable = true)
    @Convert(converter = ListFileContentToStringConverter::class)
    val fileContents: List<MemoryMarbleContent.FileContent> = mutableListOf(),
) {
    fun toModel(): MemoryMarbleContent {
        return MemoryMarbleContent(
            description = description,
            fileContents = fileContents,
        )
    }

    companion object {
        @JvmStatic
        fun from(memoryMarbleContent: MemoryMarbleContent): MemoryMarbleContentJpaModel {
            return with(memoryMarbleContent) {
                MemoryMarbleContentJpaModel(
                    description = description,
                    fileContents = fileContents,
                )
            }
        }
    }
}

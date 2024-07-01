package com.insideout.v1.endpoint.requestField

import com.insideout.model.memoryMarble.model.MemoryMarbleContent

data class MemoryMarbleContentField(
    val description: String,
    val fileIds: List<Long>,
) {
    fun toFileDefinition(): List<MemoryMarbleContent.FileContent> {
        return fileIds.map {
            MemoryMarbleContent.FileContent(
                id = it,
                fileName = DEFAULT,
            )
        }
    }

    companion object {
        private const val DEFAULT = "DEFINITION"
    }
}

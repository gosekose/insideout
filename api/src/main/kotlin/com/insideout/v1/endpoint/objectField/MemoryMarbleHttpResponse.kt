package com.insideout.v1.endpoint.objectField

import com.insideout.model.memoryMarble.MemoryMarble
import com.insideout.model.memoryMarble.model.MemoryMarbleContent
import com.insideout.model.memoryMarble.type.StoreType

data class MemoryMarbleHttpResponse(
    val id: Long,
    var feelings: List<FeelingHttpResponse>,
    var content: MemoryMarbleContentHttpField,
    var storeType: StoreType,
) {
    data class MemoryMarbleContentHttpField(
        val description: String?,
        val fileContents: List<FileContentField>,
    ) {
        data class FileContentField(
            val id: Long,
            val fileName: String,
        ) {
            companion object {
                @JvmStatic
                fun from(fileContent: MemoryMarbleContent.FileContent): FileContentField {
                    return with(fileContent) {
                        FileContentField(
                            id = id,
                            fileName = fileName,
                        )
                    }
                }
            }
        }

        companion object {
            @JvmStatic
            fun from(content: MemoryMarbleContent): MemoryMarbleContentHttpField {
                return MemoryMarbleContentHttpField(
                    description = content.description,
                    fileContents = content.fileContents.map(FileContentField::from),
                )
            }
        }
    }

    companion object {
        @JvmStatic
        fun from(memoryMarble: MemoryMarble): MemoryMarbleHttpResponse {
            return with(memoryMarble) {
                MemoryMarbleHttpResponse(
                    id = id,
                    feelings = feelings.map(FeelingHttpResponse.Companion::from),
                    content = memoryMarbleContent.let(MemoryMarbleContentHttpField.Companion::from),
                    storeType = storeType,
                )
            }
        }
    }
}

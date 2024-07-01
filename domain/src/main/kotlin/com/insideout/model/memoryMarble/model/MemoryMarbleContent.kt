package com.insideout.model.memoryMarble.model

import com.insideout.model.file.FileMetadata

data class MemoryMarbleContent(
    val description: String?,
    val fileContents: List<FileContent>,
) {
    data class FileContent(
        val id: Long,
        val fileName: String,
    )

    companion object {
        @JvmStatic
        fun of(
            description: String?,
            fileMetadatas: List<FileMetadata>,
        ): MemoryMarbleContent {
            return MemoryMarbleContent(
                description = description,
                fileContents =
                    fileMetadatas.map {
                        with(it) {
                            FileContent(
                                id = id,
                                fileName = originalFileName,
                            )
                        }
                    },
            )
        }
    }
}

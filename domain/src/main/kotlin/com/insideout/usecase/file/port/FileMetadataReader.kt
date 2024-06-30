package com.insideout.usecase.file.port

import com.insideout.model.file.FileMetadata

interface FileMetadataReader {
    fun getByIdOrNull(id: Long): FileMetadata?
}

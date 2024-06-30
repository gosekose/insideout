package com.insideout.usecase.file.port

import com.insideout.model.file.FileMetadata

interface FileMetadataSaver {
    fun save(fileMetadata: FileMetadata): FileMetadata
}

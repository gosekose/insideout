package com.insideout.adapter

import com.insideout.model.file.PresignedUrl
import com.insideout.usecase.file.port.FileManagementPort

class FileManagementAdapter : FileManagementPort {
    override fun generatePresignedUrl(fileKey: String, durationMillis: Long): PresignedUrl {
        TODO("Not yet implemented")
    }
}
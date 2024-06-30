package com.insideout.model.file

data class PresignedUrl(
    val url: String,
    val fileKey: String,
    val vendor: FileMetadata.Vendor,
)
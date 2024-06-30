package com.insideout.file.adapter

import com.insideout.annotation.ReadOnlyTransactional
import com.insideout.file.repository.FileMetadataJpaRepository
import com.insideout.model.file.FileMetadata
import com.insideout.usecase.file.port.FileMetadataReader
import org.springframework.stereotype.Repository

@Repository
@ReadOnlyTransactional
class FileMetadataReaderAdapter(
    private val fileMetadataJpaRepository: FileMetadataJpaRepository,
) : FileMetadataReader {
    override fun getByIdOrNull(id: Long): FileMetadata? {
        return fileMetadataJpaRepository.findByIdAndStatus(id)?.toModel()
    }
}

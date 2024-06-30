package com.insideout.file.adapter

import com.insideout.annotation.WritableTransactional
import com.insideout.file.model.FileMetadataJpaEntity
import com.insideout.file.repository.FileMetadataJpaRepository
import com.insideout.model.file.FileMetadata
import com.insideout.usecase.file.port.FileMetadataSaver
import org.springframework.stereotype.Repository

@Repository
@WritableTransactional
class FileMetadataSaverAdapter(
    private val fileMetadataJpaRepository: FileMetadataJpaRepository,
) : FileMetadataSaver {
    override fun save(fileMetadata: FileMetadata): FileMetadata {
        return fileMetadataJpaRepository.save(FileMetadataJpaEntity.from(fileMetadata)).toModel()
    }
}

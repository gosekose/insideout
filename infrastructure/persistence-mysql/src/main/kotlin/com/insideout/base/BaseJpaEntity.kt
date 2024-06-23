package com.insideout.base

import com.insideout.model.BaseDomainModel
import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import java.time.Instant

@MappedSuperclass
abstract class BaseJpaEntity {
    @Column(name = "created_at", columnDefinition = "datetime(6)", updatable = false, nullable = false)
    var createdAt: Instant = Instant.now()

    @Column(name = "last_modified_at", columnDefinition = "datetime(6)", nullable = false)
    var lastModifiedAt: Instant = Instant.now()
}

fun <D : BaseDomainModel> D.applyWithEntity(entity: BaseJpaEntity): D =
    this.apply {
        this.createdAt = entity.createdAt
        this.lastModifiedAt = entity.lastModifiedAt
    }

fun <D : BaseJpaEntity> D.applyWithDomainModel(domainModel: BaseDomainModel): D =
    this.apply {
        if (domainModel.isInitializedCreatedAt) {
            this.createdAt = domainModel.createdAt
        }

        if (domainModel.isInitializedLastModifiedAt) {
            this.lastModifiedAt = domainModel.lastModifiedAt
        }
    }

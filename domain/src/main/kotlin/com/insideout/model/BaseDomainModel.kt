package com.insideout.model

import java.time.Instant

abstract class BaseDomainModel {
    abstract val id: Long
    lateinit var createdAt: Instant
    lateinit var lastModifiedAt: Instant

    val isInitializedCreatedAt: Boolean
        get() = ::createdAt.isInitialized

    val isInitializedLastModifiedAt: Boolean
        get() = ::lastModifiedAt.isInitialized
}

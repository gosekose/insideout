package com.insideout.model

import com.insideout.exception.ApplicationBusinessException
import com.insideout.exception.BusinessErrorCause
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

fun <T : BaseDomainModel> T?.notnull(businessErrorCause: BusinessErrorCause): T {
    return this ?: throw ApplicationBusinessException(businessErrorCause)
}

inline fun <T : BaseDomainModel> T?.notnull(loggingMessage: (BusinessErrorCause) -> String = { "" }): T {
    if (this == null) {
        throw ApplicationBusinessException(
            BusinessErrorCause.NOT_FOUND,
            loggingMessage(BusinessErrorCause.NOT_FOUND),
            emptyMap(),
        )
    }
    return this
}

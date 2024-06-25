package com.insideout.exception

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

class ApplicationBusinessException : RuntimeException {
    val businessErrorCause: BusinessErrorCause
    val loggingMessage: String
    val details: Map<String, Any?>

    constructor(
        businessErrorCause: BusinessErrorCause,
    ) : super(businessErrorCause.message) {
        this.businessErrorCause = businessErrorCause
        this.loggingMessage = businessErrorCause.message
        this.details = emptyMap()
    }

    constructor(
        businessErrorCause: BusinessErrorCause,
        loggingMessage: String,
        details: Map<String, Any?>,
    ) : super(businessErrorCause.message) {
        this.businessErrorCause = businessErrorCause
        this.loggingMessage = loggingMessage
        this.details = details
    }
}

@OptIn(ExperimentalContracts::class)
inline fun requireBusiness(
    condition: Boolean,
    errorCause: BusinessErrorCause,
    details: Map<String, Any?> = emptyMap(),
    loggingMessage: (BusinessErrorCause) -> String = { "" },
) {
    contract {
        returns() implies condition
    }

    if (!condition) {
        throw ApplicationBusinessException(errorCause, loggingMessage(errorCause), details)
    }
}

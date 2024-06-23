package com.insideout.v1.envelope

import java.time.Instant

abstract class ResponseEnvelope<T>(
    open val type: HttpType,
    open val timestampUtc: Long = Instant.now().toEpochMilli(),
) {
    abstract val body: T?

    companion object {
        fun <T> ok(payload: T) = OkResponseEnvelope(payload)

        fun error(
            message: String,
            code: String,
            details: Map<String, Any?>,
        ) = ErrorResponseEnvelope.of(
            message = message,
            code = code,
            details = details,
        )
    }
}

enum class HttpType {
    OK,
    ERROR,
}

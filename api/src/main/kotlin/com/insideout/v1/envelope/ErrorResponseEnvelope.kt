package com.insideout.v1.envelope

data class ErrorResponseEnvelope(
    override val body: Body,
) : ResponseEnvelope<ErrorResponseEnvelope.Body>(HttpType.ERROR) {
    data class Body(
        val message: String,
        val code: String,
        val details: Map<String, Any?>?,
    )

    companion object {
        fun of(
            message: String,
            code: String,
            details: Map<String, Any?>,
        ) = ErrorResponseEnvelope(
            body =
                Body(
                    message = message,
                    code = code,
                    details = details,
                ),
        )
    }
}

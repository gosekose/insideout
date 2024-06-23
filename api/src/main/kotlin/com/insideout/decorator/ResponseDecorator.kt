package com.insideout.decorator

import com.insideout.envelope.ErrorResponseEnvelope
import com.insideout.envelope.ResponseEnvelope
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@RestControllerAdvice
class ResponseDecorator : ResponseBodyAdvice<Any> {
    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Boolean {
        val methodReturnType = returnType.method?.returnType ?: return false
        return methodReturnType.kotlin !in NOT_SUPPORT_RESPONSE_RETURN_TYPE
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse,
    ): Any? {
        return ResponseEnvelope.ok(body)
    }

    companion object {
        private val NOT_SUPPORT_RESPONSE_RETURN_TYPE =
            setOf(
                ResponseEntity::class,
                ErrorResponseEnvelope::class,
            )
    }
}

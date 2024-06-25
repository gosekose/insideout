package com.insideout.advice

import com.insideout.envelope.ErrorResponseEnvelope
import com.insideout.exception.ApplicationBusinessException
import com.insideout.exception.BusinessErrorCause
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException

@RestControllerAdvice
class ExceptionControllerAdvice {
    private val logger = LoggerFactory.getLogger(ExceptionControllerAdvice::class.java)

    @ExceptionHandler(ApplicationBusinessException::class)
    fun handleException(businessException: ApplicationBusinessException): ResponseEntity<ErrorResponseEnvelope> {
        return when (businessException.businessErrorCause) {
            BusinessErrorCause.UNAUTHORIZED ->
                buildErrorResponseEnvelope(
                    businessException,
                    HttpStatus.UNAUTHORIZED,
                )

            BusinessErrorCause.MEMBER_NOT_FOUND,
            BusinessErrorCause.MEMORY_MARBLE_NOT_FOUND,
            BusinessErrorCause.NOT_FOUND,
            ->
                buildErrorResponseEnvelope(
                    businessException,
                    HttpStatus.NOT_FOUND,
                )

            BusinessErrorCause.EMAIL_ALREADY_EXISTS ->
                buildErrorResponseEnvelope(
                    businessException,
                    HttpStatus.CONFLICT,
                )

            else ->
                buildErrorResponseEnvelope(
                    businessException,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                )
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(exception: NoHandlerFoundException): ErrorResponseEnvelope {
        val message = "Handler not found [${exception.httpMethod} ${exception.requestURL}]"
        return ErrorResponseEnvelope.of(
            code = HttpStatus.NOT_FOUND.name,
            message = message,
            details = emptyMap(),
        )
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        IllegalArgumentException::class,
        NullPointerException::class,
    )
    fun handleBadRequest(exception: IllegalArgumentException): ErrorResponseEnvelope {
        logger.error("[Exception = ${exception.message}]")
        return ErrorResponseEnvelope.of(
            code = HttpStatus.BAD_REQUEST.name,
            message = HttpStatus.BAD_REQUEST.name,
            details = emptyMap(),
        )
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleOtherException(exception: Exception): ErrorResponseEnvelope {
        logger.error("[Exception = ${exception.message}]")
        return ErrorResponseEnvelope.of(
            code = HttpStatus.INTERNAL_SERVER_ERROR.name,
            message = HttpStatus.INTERNAL_SERVER_ERROR.name,
            details = emptyMap(),
        )
    }

    private fun buildErrorResponseEnvelope(
        applicationBusinessException: ApplicationBusinessException,
        httpStatus: HttpStatus,
    ): ResponseEntity<ErrorResponseEnvelope> {
        return ResponseEntity.status(httpStatus).body(
            ErrorResponseEnvelope.of(
                code = httpStatus.name,
                message = applicationBusinessException.message ?: httpStatus.name,
                details = applicationBusinessException.details,
            ),
        )
    }
}

package com.insideout.v1.validator.annotation

import com.insideout.v1.validator.PaginationRequestValidator
import jakarta.validation.Constraint
import kotlin.reflect.KClass

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PaginationRequestValidator::class])
annotation class PaginationValidationContract(
    val message: String = "조회 파라미터 요청이 올바르지 않습니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<*>> = [],
)

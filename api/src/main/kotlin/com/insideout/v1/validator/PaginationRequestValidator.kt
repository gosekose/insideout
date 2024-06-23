package com.insideout.v1.validator

import com.insideout.v1.endpoint.paginationField.PaginationRequestParam
import com.insideout.v1.validator.annotation.PaginationValidationContract
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class PaginationRequestValidator : ConstraintValidator<PaginationValidationContract, PaginationRequestParam> {
    override fun isValid(
        value: PaginationRequestParam,
        context: ConstraintValidatorContext,
    ): Boolean {
        val (size, lastId) = value
        return (size in 1..99) && (lastId > 0)
    }
}

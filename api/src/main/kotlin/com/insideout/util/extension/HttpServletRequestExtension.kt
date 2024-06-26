package com.insideout.util.extension

import com.insideout.exception.ApplicationBusinessException
import com.insideout.exception.BusinessErrorCause
import jakarta.servlet.http.HttpServletRequest
import org.springframework.util.AntPathMatcher

fun HttpServletRequest.getPathVariable(
    name: String,
    pattern: String,
): String {
    val matcher = AntPathMatcher()
    val variables = matcher.extractUriTemplateVariables(pattern, this.requestURI)
    return variables[name] ?: throw ApplicationBusinessException(BusinessErrorCause.UNAUTHORIZED)
}

fun HttpServletRequest.getMemberId(): Long {
    return this.getHeader("memberId").toLong()
}

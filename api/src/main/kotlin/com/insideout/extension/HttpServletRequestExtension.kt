package com.insideout.extension

import jakarta.servlet.http.HttpServletRequest
import org.springframework.util.AntPathMatcher

fun HttpServletRequest.getPathVariable(
    name: String,
    pattern: String,
): String {
    val matcher = AntPathMatcher()
    val variables = matcher.extractUriTemplateVariables(pattern, this.requestURI)
    return variables[name] ?: throw IllegalArgumentException()
}

fun HttpServletRequest.getMemberId(): Long {
    return this.getHeader("memberId").toLong()
}

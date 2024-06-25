package com.insideout.model.member.model

import java.util.regex.Pattern

data class Email(
    val email: String,
) {
    init {
        val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        require(Pattern.matches(emailPattern, email)) {
            "Invalid email format"
        }
    }
}

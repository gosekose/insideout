package com.insideout.usecase.member

import com.insideout.model.member.Member
import com.insideout.model.member.model.Email

interface RegisterEmailUseCase {
    fun execute(registerEmailCommand: RegisterEmailCommand): Member

    data class RegisterEmailCommand(
        val memberId: Long,
        val email: Email,
    )
}
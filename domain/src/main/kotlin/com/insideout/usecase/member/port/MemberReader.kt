package com.insideout.usecase.member.port

import com.insideout.model.member.Member
import com.insideout.model.member.model.Email

interface MemberReader {
    fun getByIdOrNull(id: Long): Member?

    fun isExistEmail(email: Email): Boolean
}

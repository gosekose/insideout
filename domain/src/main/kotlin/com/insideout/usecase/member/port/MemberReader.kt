package com.insideout.usecase.member.port

import com.insideout.model.member.model.Member

interface MemberReader {
    fun getByIdOrNull(id: Long): Member?
}

package com.insideout.usecase.member.port

import com.insideout.model.member.Member

interface MemberSaver {
    fun save(member: Member): Member
}

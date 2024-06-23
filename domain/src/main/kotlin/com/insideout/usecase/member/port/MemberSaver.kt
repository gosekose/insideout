package com.insideout.usecase.member.port

import com.insideout.model.member.model.Member

interface MemberSaver {
    fun save(member: Member): Member
}

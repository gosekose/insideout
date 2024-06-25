package com.insideout.model.member

import com.insideout.model.BaseDomainModel
import com.insideout.model.member.model.Email

sealed class Member : BaseDomainModel() {
    abstract val version: Version
    abstract var email: Email?

    data class V1(
        override val id: Long = 0L,
        override var email: Email?,
    ) : Member() {
        override val version: Version = Version.VERSION_V1

        fun registerEmail(email: Email) =
            this.apply {
                this.email = email
            }
    }

    enum class Version {
        VERSION_V1,
    }
}

package com.insideout.model.member.model

import com.insideout.model.BaseDomainModel

sealed class Member : BaseDomainModel() {
    abstract val version: Version

    data class V1(
        override val id: Long = 0L,
    ) : Member() {
        override val version: Version = Version.VERSION_V1
    }

    enum class Version {
        VERSION_V1,
    }
}

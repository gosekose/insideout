package com.insideout.member.model

import com.insideout.base.BaseJpaEntity
import com.insideout.base.applyWithDomainModel
import com.insideout.base.applyWithEntity
import com.insideout.model.member.Member
import com.insideout.model.member.model.Email
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table

@Entity
@Table(
    name = "member",
    indexes = [
        Index(name = "idx_member__created_at", columnList = "created_at"),
        Index(name = "idx_member__email", columnList = "email"),
    ],
)
class MemberJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Enumerated(value = EnumType.STRING)
    @Column(name = "version", columnDefinition = "varchar(32)", nullable = false)
    val version: Member.Version,
    @Column(name = "email", columnDefinition = "varchar(255)", nullable = true)
    val email: String?,
) : BaseJpaEntity() {
    fun toModel(): Member {
        return when (version) {
            Member.Version.VERSION_V1 -> Member.V1(
                id = id,
                email = email?.let(::Email)
            )
        }.applyWithEntity(this)
    }

    companion object {
        @JvmStatic
        fun from(member: Member): MemberJpaEntity {
            return with(member) {
                when (member.version) {
                    Member.Version.VERSION_V1 -> {
                        val memberV1 = member as Member.V1
                        MemberJpaEntity(
                            id = id,
                            version = version,
                            email = memberV1.email?.email
                        )
                    }
                }
            }.applyWithDomainModel(member)
        }
    }
}

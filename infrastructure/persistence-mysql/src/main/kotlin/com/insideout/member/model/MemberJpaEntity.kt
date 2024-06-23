package com.insideout.member.model

import com.insideout.base.BaseJpaEntity
import com.insideout.base.applyWithDomainModel
import com.insideout.base.applyWithEntity
import com.insideout.model.member.model.Member
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
    ],
)
class MemberJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Enumerated(value = EnumType.STRING)
    @Column(name = "version", columnDefinition = "varchar(32)", nullable = false)
    val version: Member.Version,
) : BaseJpaEntity() {
    fun toModel(): Member {
        return when (version) {
            Member.Version.VERSION_V1 -> Member.V1(id)
        }.applyWithEntity(this)
    }

    companion object {
        @JvmStatic
        fun from(member: Member): MemberJpaEntity {
            return with(member) {
                when (member.version) {
                    Member.Version.VERSION_V1 ->
                        MemberJpaEntity(
                            id = id,
                            version = version,
                        )
                }
            }.applyWithDomainModel(member)
        }
    }
}

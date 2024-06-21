package com.insideout.base

import org.springframework.data.jpa.repository.JpaRepository

interface BaseJpaRepository<T : BaseJpaEntity, ID : Any> : JpaRepository<T, ID>

fun <T : BaseJpaEntity> T?.notnull(message: () -> String): T {
    // TODO: 수정
    return this ?: throw IllegalArgumentException()
}

fun <T : BaseJpaEntity> T?.notnull(): T {
    // TODO: 수정
    return this ?: throw IllegalArgumentException()
}

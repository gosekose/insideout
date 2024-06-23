package com.insideout.base

fun <T : BaseJpaEntity> T?.notnull(message: () -> String): T {
    // TODO: 수정
    return this ?: throw IllegalArgumentException()
}

fun <T : BaseJpaEntity> T?.notnull(): T {
    // TODO: 수정
    return this ?: throw IllegalArgumentException()
}

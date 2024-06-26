package com.insideout.distributeLock

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class DistributedLockBeforeTransaction(
    val key: Array<String>,
    val name: String = "",
    val separator: String = ":",
    val transactionalReadOnly: Boolean = false,
)

package com.insideout.distributeLock

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class DistributedLock(
    val key: Array<String>,
    val prefix: DistributedLockPrefixKey,
    val separator: String = ":",
)

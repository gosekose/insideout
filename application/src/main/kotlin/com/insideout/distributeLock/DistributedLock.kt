package com.insideout.distributeLock

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class DistributedLock(
    val key: Array<String>,
    val name: String = "",
    val separator: String = ":",
)

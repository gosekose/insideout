package com.insideout.lock

interface DistributedLockManager {
    fun <T> lock(
        key: String,
        block: () -> T,
    ): T
}

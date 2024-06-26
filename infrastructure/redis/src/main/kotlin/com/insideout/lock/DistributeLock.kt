package com.insideout.lock

interface DistributeLock {
    fun tryLock(): Boolean

    fun unlock()
}

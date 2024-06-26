package com.insideout.lock

import com.insideout.RedisPort
import org.springframework.stereotype.Component

@Component
class RedisDistributedLockManager(
    private val redisPort: RedisPort,
) : AbstractDistributedLockManager() {
    override fun generateLock(key: String): DistributeLock {
        return RedisDistributeLock(REDIS_LOCK_PREFIX + key)
    }

    inner class RedisDistributeLock(private val key: String) : DistributeLock {
        override fun tryLock(): Boolean {
            return redisPort.setIfAbsent(key, true, DISTRIBUTE_LOCK_DURATION_MILLIS)
        }

        override fun unlock() {
            redisPort.delete(key)
        }

        override fun toString(): String = "$REDIS_LOCK($key)"
    }

    companion object {
        private const val DISTRIBUTE_LOCK_DURATION_MILLIS = 60000L
        private const val REDIS_LOCK = "RedisLock"
        private const val REDIS_LOCK_PREFIX = "lock:"
    }
}

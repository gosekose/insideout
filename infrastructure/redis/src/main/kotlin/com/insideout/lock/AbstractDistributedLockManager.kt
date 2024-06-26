package com.insideout.lock

import com.insideout.exception.ApplicationBusinessException
import com.insideout.exception.BusinessErrorCause
import org.slf4j.LoggerFactory
import org.springframework.transaction.support.TransactionSynchronizationManager

abstract class AbstractDistributedLockManager : DistributedLockManager {
    protected val logger = LoggerFactory.getLogger(AbstractDistributedLockManager::class.java)

    abstract fun generateLock(key: String): DistributeLock

    override fun <T> lock(
        key: String,
        block: () -> T,
    ): T {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            logger.error("DistributedLock 트랜잭션 밖에서 실행되어야 합니다.")
            throw ApplicationBusinessException(BusinessErrorCause.INTERNAL_SERVER_ERROR)
        }

        val lock = generateLock(key)

        if (!lock.tryLock()) {
            throw ApplicationBusinessException(BusinessErrorCause.DISTRIBUTE_LOCK_TRY_FAILED)
        }

        return try {
            block()
        } finally {
            lock.unlock()
        }
    }
}

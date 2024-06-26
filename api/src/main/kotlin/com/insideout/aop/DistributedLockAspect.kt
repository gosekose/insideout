package com.insideout.aop

import com.insideout.distributeLock.DistributedLock
import com.insideout.distributeLock.DistributedLockBeforeTransaction
import com.insideout.lock.DistributedLockManager
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.expression.EvaluationContext
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate

@Order(1)
@Component
@Aspect
class DistributedLockAspect(
    private val distributedLockManager: DistributedLockManager,
    private val transactionManager: PlatformTransactionManager,
    private val applicationContext: ApplicationContext,
) {
    @Around("@annotation(distributedLock)")
    fun round(
        joinPoint: ProceedingJoinPoint,
        distributedLock: DistributedLock,
    ): Any? {
        val lockKey =
            with(distributedLock) {
                lockKey(
                    joinPoint = joinPoint,
                    key = key,
                    name = name,
                    separator = separator,
                )
            }
        return distributedLockManager.lock(lockKey) {
            joinPoint.proceed()
        }
    }

    @Around("@annotation(distributedLockBeforeTransaction)")
    fun around(
        joinPoint: ProceedingJoinPoint,
        distributedLockBeforeTransaction: DistributedLockBeforeTransaction,
    ): Any? {
        val lockKey =
            with(distributedLockBeforeTransaction) {
                lockKey(
                    joinPoint = joinPoint,
                    key = key,
                    name = name,
                    separator = separator,
                )
            }
        return distributedLockManager.lock(lockKey) {
            val transactionTemplate =
                TransactionTemplate(transactionManager).apply {
                    isReadOnly = distributedLockBeforeTransaction.transactionalReadOnly
                }
            transactionTemplate.execute {
                joinPoint.proceed()
            }
        }
    }

    private fun lockKey(
        joinPoint: ProceedingJoinPoint,
        key: Array<String>,
        name: String,
        separator: String,
    ): String {
        val evaluationContext = createEvaluationContext(joinPoint)
        return key.asSequence()
            .map { EXPRESSION_PARSER.parseExpression(it) }
            .map {
                requireNotNull(it.getValue(evaluationContext)) {
                    "DistributedLock Key가 잘못되었습니다."
                }
            }
            .joinToString(
                separator = separator,
                prefix = "${name}$separator",
            )
    }

    private fun createEvaluationContext(joinPoint: ProceedingJoinPoint): EvaluationContext {
        val parameters = joinPoint.parameters()
        return StandardEvaluationContext().apply { setVariables(parameters) }
    }

    private fun JoinPoint.parameters() = (signature as MethodSignature).parameterNames.asSequence().zip(args.asSequence()).toMap()

    companion object {
        private val EXPRESSION_PARSER: ExpressionParser = SpelExpressionParser()
    }
}

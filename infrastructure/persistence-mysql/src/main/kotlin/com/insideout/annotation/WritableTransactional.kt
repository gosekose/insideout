package com.insideout.annotation

import org.springframework.transaction.annotation.Transactional

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Transactional(readOnly = false)
annotation class WritableTransactional

package com.insideout.cache

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CacheableAnnotation(
    val cacheName: String,
    val key: String,
    val durationMillis: Long,
)

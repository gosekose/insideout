package com.insideout

interface RedisPort {
    fun <T> getOrNull(
        key: String,
        type: Class<T>,
    ): T?

    fun set(
        key: String,
        value: Any,
    )

    fun setIfAbsent(
        key: String,
        value: Any,
    ): Boolean

    fun setIfAbsent(
        key: String,
        value: Any,
        durationMillis: Long,
    ): Boolean

    fun delete(key: String)
}

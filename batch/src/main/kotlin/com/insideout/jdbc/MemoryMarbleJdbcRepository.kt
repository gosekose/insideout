package com.insideout.jdbc

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

interface MemoryMarbleJdbcRepository {
    fun updateStoreTypeDailyToPermanentBatch(ids: List<Long>)
}

@Repository
class MemoryMarbleJdbcRepositoryImpl(
    @Qualifier("jdbcTemplate") private val jdbcTemplate: JdbcTemplate,
) : MemoryMarbleJdbcRepository {
    override fun updateStoreTypeDailyToPermanentBatch(ids: List<Long>) {
        val batchArgs = ids.map { arrayOf<Any>(it) }
        jdbcTemplate.batchUpdate(UPDATE_SQL, batchArgs)
    }

    companion object {
        const val UPDATE_SQL = "UPDATE memory_marbles SET store_type = 'PERMANENT' WHERE id = ?"
    }
}

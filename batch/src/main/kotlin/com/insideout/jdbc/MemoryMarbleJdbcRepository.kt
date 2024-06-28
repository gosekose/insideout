package com.insideout.jdbc

import com.insideout.jdbc.rowMapper.EntityRowMapper
import com.insideout.memoryMarble.model.MemoryMarbleJpaEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

interface MemoryMarbleJdbcRepository {
    fun updateStoreTypeDailyToPermanentBatch(ids: List<Long>)

    fun saveInBatch(memoryMarbleJpaEntities: List<MemoryMarbleJpaEntity>)
}

@Repository
class MemoryMarbleJdbcRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate,
    private val simpleJdbcInsert: SimpleJdbcInsert,
) : MemoryMarbleJdbcRepository {

    override fun updateStoreTypeDailyToPermanentBatch(ids: List<Long>) {
        val batchArgs = ids.map { arrayOf<Any>(it) }
        jdbcTemplate.batchUpdate(UPDATE_SQL, batchArgs)
    }

    override fun saveInBatch(memoryMarbleJpaEntities: List<MemoryMarbleJpaEntity>) {
        simpleJdbcInsert.executeBatch(*generateMapSqlParameterSource(memoryMarbleJpaEntities))
    }

    private fun generateMapSqlParameterSource(memoryMarbleJpaEntities: List<MemoryMarbleJpaEntity>): Array<SqlParameterSource> {
        return memoryMarbleJpaEntities.map { EntityRowMapper.mapSqlParameterSourceWith(it) }.toTypedArray()
    }

    companion object {
        const val UPDATE_SQL = "UPDATE memory_marbles SET store_type = 'PERMANENT' WHERE id = ?"
    }
}

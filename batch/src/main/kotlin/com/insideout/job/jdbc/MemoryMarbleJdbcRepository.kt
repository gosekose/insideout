package com.insideout.job.jdbc

import com.insideout.memoryMarble.model.MemoryMarbleJpaEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

interface MemoryMarbleJdbcRepository {
    fun deleteInBatchWithJdbc(ids: List<Long>)

    fun saveInBatch(memoryMarbleJpaEntities: List<MemoryMarbleJpaEntity>)
}

@Repository
class MemoryMarbleJdbcRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate,
    private val simpleJdbcInsert: SimpleJdbcInsert,
) : MemoryMarbleJdbcRepository {
    override fun deleteInBatchWithJdbc(ids: List<Long>) {
        val sql = "DELETE FROM memory_marbles WHERE id = ?"
        val batchArgs = ids.map { arrayOf<Any>(it) }
        jdbcTemplate.batchUpdate(sql, batchArgs)
    }

    override fun saveInBatch(memoryMarbleJpaEntities: List<MemoryMarbleJpaEntity>) {
        simpleJdbcInsert.executeBatch(*generateMapSqlParameterSource(memoryMarbleJpaEntities))
    }

    private fun generateMapSqlParameterSource(memoryMarbleJpaEntities: List<MemoryMarbleJpaEntity>): Array<SqlParameterSource> {
        return memoryMarbleJpaEntities.map {
            with(it) {
                MapSqlParameterSource()
                    .addValue("id", id)
                    .addValue("member_id", memberId)
                    .addValue("description", content.description)
                    .addValue("feeling_ids", feelingIds)
                    .addValue("store_type", storeType)
                    .addValue("status", softDeleteStatus)
                    .addValue("created_at", createdAt)
                    .addValue("last_modified_at", lastModifiedAt)
            }
        }.toTypedArray()
    }
}

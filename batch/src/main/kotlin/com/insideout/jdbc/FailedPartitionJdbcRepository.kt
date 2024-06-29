package com.insideout.jdbc

import com.insideout.jdbc.dao.FailedPartition
import com.insideout.jdbc.rowMapper.DaoRowMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

interface FailedPartitionJdbcRepository {
    fun save(failedPartition: FailedPartition)
    fun saveAll(failedPartitions: List<FailedPartition>)
}

@Repository
class FailedPartitionJdbcRepositoryImpl(
    @Qualifier("batchSimpleJdbcInsert") private val simpleJdbcInsert: SimpleJdbcInsert,
) : FailedPartitionJdbcRepository {
    init {
        simpleJdbcInsert
            .withTableName("FAILED_PARTITIONS")
            .usingGeneratedKeyColumns("id")
            .usingColumns("min_id", "max_id", "step_execution_id", "created_at", "last_modified_at", "status")
    }

    override fun save(failedPartition: FailedPartition) {
        simpleJdbcInsert.executeBatch(generateMapSqlParameterSource(failedPartition))
    }

    override fun saveAll(failedPartitions: List<FailedPartition>) {
        simpleJdbcInsert.executeBatch(*generateMapSqlParameterSource(failedPartitions))
    }

    private fun generateMapSqlParameterSource(failedPartition: FailedPartition): SqlParameterSource {
        return failedPartition.let { DaoRowMapper.mapSqlParameterSourceWith(it) }
    }

    private fun generateMapSqlParameterSource(failedPartitions: List<FailedPartition>): Array<SqlParameterSource> {
        return failedPartitions.map { DaoRowMapper.mapSqlParameterSourceWith(it) }.toTypedArray()
    }
}

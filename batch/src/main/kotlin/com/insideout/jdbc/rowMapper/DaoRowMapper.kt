package com.insideout.jdbc.rowMapper

import com.insideout.base.SoftDeleteStatus
import com.insideout.jdbc.dao.FailedPartition
import com.insideout.memoryMarble.model.MemoryMarbleJpaEntity
import com.insideout.memoryMarble.model.model.MemoryMarbleContentJpaModel
import com.insideout.model.memoryMarble.type.StoreType
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import java.sql.ResultSet

object DaoRowMapper {
    val memoryMarbleEntityRowMapper =
        RowMapper { rs: ResultSet, _: Int ->
            MemoryMarbleJpaEntity(
                id = rs.getLong("id"),
                memberId = rs.getLong("member_id"),
                content = MemoryMarbleContentJpaModel(description = rs.getString("description")),
                feelingIds = rs.getString("feeling_ids").split(",").map { it.toLong() },
                storeType = StoreType.valueOf(rs.getString("store_type")),
                softDeleteStatus =
                    SoftDeleteStatus.valueOf(
                        rs.getString("status"),
                    ),
            ).apply {
                createdAt = rs.getTimestamp("created_at").toInstant()
                lastModifiedAt = rs.getTimestamp("last_modified_at").toInstant()
            }
        }

    fun mapSqlParameterSourceWith(memoryMarbleJpaEntity: MemoryMarbleJpaEntity): MapSqlParameterSource {
        return with(memoryMarbleJpaEntity) {
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
    }

    fun mapSqlParameterSourceWith(failedPartition: FailedPartition): MapSqlParameterSource {
        return with(failedPartition) {
            MapSqlParameterSource()
                .addValue("min_id", minId)
                .addValue("max_id", maxId)
                .addValue("job_execution_Id", jobExecutionId)
                .addValue("created_at", createdAt)
                .addValue("last_modified_at", lastModifiedAt)
                .addValue("status", status)
        }
    }
}

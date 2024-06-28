package com.insideout.partition

import org.springframework.batch.core.partition.support.Partitioner
import org.springframework.batch.item.ExecutionContext
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class RangePartitioner(
    private val jdbcTemplate: JdbcTemplate,
) : Partitioner {
    override fun partition(gridSize: Int): MutableMap<String, ExecutionContext> {
        val result = mutableMapOf<String, ExecutionContext>()

        val minId = jdbcTemplate.queryForObject("SELECT MIN(id) FROM memory_marbles", Long::class.java)
        val maxId = jdbcTemplate.queryForObject("SELECT MAX(id) FROM memory_marbles", Long::class.java)

        if (minId == null || maxId == null) {
            return result
        }

        val targetSize = (maxId - minId) / gridSize + 1

        var start = minId.toLong()
        var end = start + targetSize - 1

        for (i in 0 until gridSize) {
            val context = ExecutionContext()
            context.putLong("minValue", start)
            context.putLong("maxValue", end)
            result["partition$i"] = context
            start += targetSize
            end = start + targetSize - 1
        }

        return result
    }
}

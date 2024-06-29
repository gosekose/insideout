package com.insideout.partition

import org.springframework.batch.core.partition.support.Partitioner
import org.springframework.batch.item.ExecutionContext

class RangePartitioner(
    private val minId: Long?,
    private val maxId: Long?,
) : Partitioner {
    override fun partition(gridSize: Int): MutableMap<String, ExecutionContext> {
        val result = mutableMapOf<String, ExecutionContext>()
        if (minId == null || maxId == null) {
            return result
        }

        val targetSize = (maxId - minId + 1) / gridSize

        var start = minId.toLong()
        var end = start + targetSize - 1

        for (i in 0 until gridSize - 1) {
            val context = ExecutionContext()
            context.putLong("minValue", start)
            context.putLong("maxValue", end)
            result["partition$i"] = context
            start += targetSize
            end = start + targetSize - 1
        }

        val context = ExecutionContext()
        context.putLong("minValue", start)
        context.putLong("maxValue", maxId)
        result["partition${gridSize - 1}"] = context

        return result
    }
}

package com.insideout.model.feeling

import com.insideout.model.BaseDomainModel
import com.insideout.model.feeling.model.FeelingMemoryMarbleConnect
import com.insideout.model.feeling.type.FeelingType

data class Feeling(
    override val id: Long = 0L,
    val memberId: Long,
    var score: Long,
    var type: FeelingType,
    var memoryMarbleConnect: FeelingMemoryMarbleConnect,
) : BaseDomainModel() {
    fun updateMemoryMarbleConnect(memoryMarbleConnect: FeelingMemoryMarbleConnect.ConnectMemoryMarble): Feeling {
        return this.apply {
            this.memoryMarbleConnect = memoryMarbleConnect
        }
    }

    fun update(
        score: Long,
        type: FeelingType,
    ): Feeling {
        return this.apply {
            this.score = score
            this.type = type
        }
    }
}

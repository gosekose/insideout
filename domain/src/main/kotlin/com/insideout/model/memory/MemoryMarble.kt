package com.insideout.model.memory

import com.insideout.model.BaseDomainModel
import com.insideout.model.feeling.Feelings
import com.insideout.model.memory.model.Content
import com.insideout.model.memory.type.StoreType

data class MemoryMarble(
    override val id: Long = 0L,
    val memberId: Long,
    var feelings: Feelings,
    var content: Content,
    var storeType: StoreType,
) : BaseDomainModel() {
    fun update(
        feelings: Feelings,
        content: Content,
    ): MemoryMarble {
        return this.apply {
            this.feelings = feelings
            this.content = content
        }
    }

    fun update(storeType: StoreType): MemoryMarble {
        return this.apply {
            this.storeType = storeType
        }
    }
}

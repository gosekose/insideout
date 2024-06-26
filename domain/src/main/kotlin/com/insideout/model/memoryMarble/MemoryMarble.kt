package com.insideout.model.memoryMarble

import com.insideout.model.BaseDomainModel
import com.insideout.model.feeling.Feelings
import com.insideout.model.memoryMarble.model.MemoryMarbleContent
import com.insideout.model.memoryMarble.type.StoreType

data class MemoryMarble(
    override val id: Long = 0L,
    val memberId: Long,
    var feelings: Feelings,
    var memoryMarbleContent: MemoryMarbleContent,
    var storeType: StoreType,
) : BaseDomainModel() {
    fun update(
        feelings: Feelings,
        memoryMarbleContent: MemoryMarbleContent,
    ): MemoryMarble {
        return this.apply {
            this.feelings = feelings
            this.memoryMarbleContent = memoryMarbleContent
        }
    }

    fun update(storeType: StoreType): MemoryMarble {
        return this.apply {
            this.storeType = storeType
        }
    }
}

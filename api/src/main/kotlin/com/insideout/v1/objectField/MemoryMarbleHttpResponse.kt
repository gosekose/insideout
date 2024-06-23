package com.insideout.v1.objectField

import com.insideout.model.memory.MemoryMarble
import com.insideout.model.memory.model.MemoryMarbleContent
import com.insideout.model.memory.type.StoreType

data class MemoryMarbleHttpResponse(
    val id: Long,
    var feelings: List<FeelingHttpResponse>,
    var content: MemoryMarbleContentHttpField,
    var storeType: StoreType,
) {
    data class MemoryMarbleContentHttpField(
        val description: String?,
    ) {
        companion object {
            @JvmStatic
            fun from(content: MemoryMarbleContent): MemoryMarbleContentHttpField {
                return MemoryMarbleContentHttpField(
                    description = content.description,
                )
            }
        }
    }

    companion object {
        @JvmStatic
        fun from(memoryMarble: MemoryMarble): MemoryMarbleHttpResponse {
            return with(memoryMarble) {
                MemoryMarbleHttpResponse(
                    id = id,
                    feelings = feelings.map(FeelingHttpResponse.Companion::from),
                    content = memoryMarbleContent.let(MemoryMarbleContentHttpField.Companion::from),
                    storeType = storeType,
                )
            }
        }
    }
}

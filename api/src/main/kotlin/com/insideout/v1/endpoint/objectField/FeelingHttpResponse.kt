package com.insideout.v1.endpoint.objectField

import com.insideout.model.feeling.Feeling
import com.insideout.model.feeling.model.FeelingMemoryMarbleConnect
import com.insideout.model.feeling.type.FeelingType

data class FeelingHttpResponse(
    val id: Long,
    var score: Long,
    var type: FeelingType,
    val connectedMemoryMarbleId: Long?,
) {
    companion object {
        @JvmStatic
        fun from(feeling: Feeling): FeelingHttpResponse {
            return with(feeling) {
                FeelingHttpResponse(
                    id = id,
                    score = score,
                    type = type,
                    connectedMemoryMarbleId =
                        when (val connect = memoryMarbleConnect) {
                            is FeelingMemoryMarbleConnect.DisConnectMemoryMarble -> null
                            is FeelingMemoryMarbleConnect.ConnectMemoryMarble -> connect.memoryMarbleId
                        },
                )
            }
        }
    }
}

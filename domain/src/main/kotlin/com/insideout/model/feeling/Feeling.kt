package com.insideout.model.feeling

import com.insideout.model.BaseDomainModel
import com.insideout.model.feeling.type.FeelingType

data class Feeling(
    override val id: Long = 0L,
    val memberId: Long,
    val score: Long,
    val type: FeelingType,
) : BaseDomainModel()

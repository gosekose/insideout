package com.insideout.usecase.feeling.port

import com.insideout.model.feeling.Feelings

interface FeelingReader {
    fun getByIds(ids: List<Long>): Feelings
}

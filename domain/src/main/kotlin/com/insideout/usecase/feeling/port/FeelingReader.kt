package com.insideout.usecase.feeling.port

import com.insideout.model.feeling.Feelings

interface FeelingReader {
    fun getAll(ids: List<Long>): Feelings
}

package com.insideout.usecase.feeling.port

import com.insideout.model.feeling.Feelings

interface FeelingRemover {
    fun remove(feelings: Feelings)
}
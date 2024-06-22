package com.insideout.usecase.feeling.port

import com.insideout.model.feeling.Feelings

interface FeelingSaver {
    fun saveAll(feelings: Feelings): Feelings
}
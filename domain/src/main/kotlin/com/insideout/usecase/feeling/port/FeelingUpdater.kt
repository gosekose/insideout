package com.insideout.usecase.feeling.port

import com.insideout.model.feeling.Feelings

interface FeelingUpdater {
    fun update(feelings: Feelings): Feelings
}
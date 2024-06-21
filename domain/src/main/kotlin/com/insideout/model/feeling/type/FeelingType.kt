package com.insideout.model.feeling.type

import com.insideout.model.feeling.type.FeelingType.Color.BLUE
import com.insideout.model.feeling.type.FeelingType.Color.GREEN
import com.insideout.model.feeling.type.FeelingType.Color.ORANGE
import com.insideout.model.feeling.type.FeelingType.Color.PURPLE
import com.insideout.model.feeling.type.FeelingType.Color.RED

enum class FeelingType(
    val color: Color,
) {
    JOY(ORANGE),
    SADNESS(BLUE),
    ANGER(RED),
    DISGUST(GREEN),
    FEAR(PURPLE),
    ;

    enum class Color {
        ORANGE,
        BLUE,
        RED,
        GREEN,
        PURPLE,
    }
}

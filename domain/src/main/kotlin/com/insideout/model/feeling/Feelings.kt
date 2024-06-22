package com.insideout.model.feeling

class Feelings(
    val feelings: List<Feeling>,
) : List<Feeling> by feelings {
    operator fun plus(other: Feelings): Feelings {
        return Feelings(feelings + other.feelings)
    }
}

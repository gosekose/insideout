package com.insideout.model.memory.model

data class MediaFile(
    val host: String,
    val path: String,
    val filename: String,
) {
    val pathWithoutHost: String
        get() = "$path/$filename"

    val publishUrl: String
        get() = "$host/$path/$filename"
}

package com.insideout.envelope

data class OkResponseEnvelope<T>(override val body: T?) : ResponseEnvelope<T>(HttpType.OK)

package com.insideout.v1.envelope

data class OkResponseEnvelope<T>(override val body: T?) : ResponseEnvelope<T>(HttpType.OK)

package com.insideout.model.memory

sealed class MemoryMarbleStore {
    abstract val marbles: MemoryMarbles

    data class DailyMemoryZone(
        override val marbles: MemoryMarbles,
    ) : MemoryMarbleStore()

    data class DiscardMemoryZone(
        override val marbles: MemoryMarbles,
    ) : MemoryMarbleStore()

    data class PermanentMemoryArchive(
        override val marbles: MemoryMarbles,
    ) : MemoryMarbleStore()
}

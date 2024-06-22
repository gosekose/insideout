package com.insideout.model.feeling.model

sealed class FeelingMemoryMarbleConnect {
    abstract val memoryMarbleConnectStatus: MemoryMarbleConnectStatus

    data object DisConnectMemoryMarble : FeelingMemoryMarbleConnect() {
        override val memoryMarbleConnectStatus: MemoryMarbleConnectStatus
            get() = MemoryMarbleConnectStatus.DISCONNECT

        fun connect(memoryMarbleId: Long): ConnectMemoryMarble {
            return ConnectMemoryMarble(
                memoryMarbleId = memoryMarbleId
            )
        }
    }

    data class ConnectMemoryMarble(
        val memoryMarbleId: Long,
    ) : FeelingMemoryMarbleConnect() {
        override val memoryMarbleConnectStatus: MemoryMarbleConnectStatus
            get() = MemoryMarbleConnectStatus.CONNECT
    }

    enum class MemoryMarbleConnectStatus {
        DISCONNECT,
        CONNECT,
    }
}
package com.insideout.distributeLock

enum class DistributedLockPrefixKey {
    /**
     * MemoryMarble
     */
    MEMORY_MARBLE_CREATE_WITH_MEMBER_ID,
    MEMORY_MARBLE_UPDATE_WITH_MEMORY_MARBLE_ID,

    /**
     * Member
     */
    MEMBER_UPDATE_WITH_MEMBER_ID,
}

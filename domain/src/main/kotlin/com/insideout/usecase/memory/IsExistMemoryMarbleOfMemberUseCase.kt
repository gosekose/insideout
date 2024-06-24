package com.insideout.usecase.memory

interface IsExistMemoryMarbleOfMemberUseCase {
    fun exist(
        memoryMarbleId: Long,
        memberId: Long,
    ): Boolean
}

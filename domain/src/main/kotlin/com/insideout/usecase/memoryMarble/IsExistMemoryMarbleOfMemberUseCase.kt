package com.insideout.usecase.memoryMarble

interface IsExistMemoryMarbleOfMemberUseCase {
    fun exist(
        memoryMarbleId: Long,
        memberId: Long,
    ): Boolean
}

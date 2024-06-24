package com.insideout.usecase.memory

import com.insideout.usecase.memory.port.MemoryMarbleReader
import org.springframework.stereotype.Service

@Service
class IsExistMemoryMarbleOfMemberService(
    private val memoryMarbleReader: MemoryMarbleReader,
) : IsExistMemoryMarbleOfMemberUseCase {
    override fun exist(
        memoryMarbleId: Long,
        memberId: Long,
    ): Boolean {
        return memoryMarbleReader.getOrNull(memoryMarbleId)?.let { it.memberId == memberId } ?: false
    }
}

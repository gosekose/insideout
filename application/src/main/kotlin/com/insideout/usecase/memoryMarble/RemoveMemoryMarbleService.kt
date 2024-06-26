package com.insideout.usecase.memoryMarble

import com.insideout.distributeLock.DistributedLockBeforeTransaction
import com.insideout.distributeLock.DistributedLockPrefixKey
import com.insideout.usecase.memoryMarble.port.MemoryMarbleRemover
import org.springframework.stereotype.Component

@Component
class RemoveMemoryMarbleService(
    private val memoryMarbleRemover: MemoryMarbleRemover,
) : RemoveMemoryMarbleUseCase {
    @DistributedLockBeforeTransaction(
        key = ["#command.id"],
        prefix = DistributedLockPrefixKey.MEMORY_MARBLE_UPDATE_WITH_MEMORY_MARBLE_ID,
        transactionalReadOnly = false,
    )
    override fun execute(command: RemoveMemoryMarbleUseCase.Command) {
        memoryMarbleRemover.remove(command.id)
    }
}

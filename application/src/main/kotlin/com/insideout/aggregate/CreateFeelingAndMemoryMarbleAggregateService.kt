package com.insideout.aggregate

import com.insideout.distributeLock.DistributedLockBeforeTransaction
import com.insideout.distributeLock.DistributedLockPrefixKey
import com.insideout.model.memoryMarble.MemoryMarble
import com.insideout.model.memoryMarble.model.MemoryMarbleContent
import com.insideout.usecase.feeling.CreateFeelingsUseCase
import com.insideout.usecase.feeling.UpdateFeelingsConnectMemoryMarbleUseCase
import com.insideout.usecase.file.port.FileMetadataReader
import com.insideout.usecase.memoryMarble.CreateMemoryMarbleUseCase
import org.springframework.stereotype.Service

@Service
class CreateFeelingAndMemoryMarbleAggregateService(
    private val fileMetadataReader: FileMetadataReader,
    private val createFeelingsUseCase: CreateFeelingsUseCase,
    private val createMemoryMarbleUseCase: CreateMemoryMarbleUseCase,
    private val updateFeelingsConnectMemoryMarbleUseCase: UpdateFeelingsConnectMemoryMarbleUseCase,
) : CreateFeelingAndMemoryMarbleAggregate {
    @DistributedLockBeforeTransaction(
        key = ["#definition.memberId"],
        prefix = DistributedLockPrefixKey.MEMORY_MARBLE_CREATE_WITH_MEMBER_ID,
        transactionalReadOnly = false,
    )
    override fun create(definition: CreateFeelingAndMemoryMarbleAggregate.Definition): MemoryMarble {
        val (memberId, feelingDefinitions, content) = definition

        val feelings =
            createFeelingsUseCase.execute(
                CreateFeelingsUseCase.Definition(
                    memberId = memberId,
                    feelingDefinitions = feelingDefinitions,
                ),
            )

        val memoryMarbleContent =
            fileMetadataReader.getByIdsAndMemberId(
                ids = content.fileContents.map { it.id },
                memberId = memberId,
            ).let { MemoryMarbleContent.of(content.description, it) }

        val memoryMarble =
            createMemoryMarbleUseCase.execute(
                CreateMemoryMarbleUseCase.Definition(
                    memberId = memberId,
                    feelings = feelings,
                    memoryMarbleContent = memoryMarbleContent,
                ),
            )

        updateFeelingsConnectMemoryMarbleUseCase.execute(
            UpdateFeelingsConnectMemoryMarbleUseCase.Command(
                memoryMarbleId = memoryMarble.id,
                feelings = feelings,
            ),
        )

        return memoryMarble
    }
}

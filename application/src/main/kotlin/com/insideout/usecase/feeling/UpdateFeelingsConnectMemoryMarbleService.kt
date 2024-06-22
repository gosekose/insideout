package com.insideout.usecase.feeling

import com.insideout.model.feeling.Feelings
import com.insideout.model.feeling.model.FeelingMemoryMarbleConnect
import com.insideout.usecase.feeling.port.FeelingUpdater
import org.springframework.stereotype.Component

@Component
class UpdateFeelingsConnectMemoryMarbleService(
    private val feelingUpdater: FeelingUpdater,
) : UpdateFeelingsConnectMemoryMarbleUseCase {
    override fun execute(command: UpdateFeelingsConnectMemoryMarbleUseCase.Command): Feelings {
        val (memoryMarbleId, feelings) = command
        return feelingUpdater.update(
            feelings.map {
                when (val memoryMarbleConnect = it.memoryMarbleConnect) {
                    is FeelingMemoryMarbleConnect.DisConnectMemoryMarble -> {
                        it.updateMemoryMarbleConnect(memoryMarbleConnect.connect(memoryMarbleId))
                    }

                    is FeelingMemoryMarbleConnect.ConnectMemoryMarble -> it
                }
            }.let(::Feelings)
        )
    }
}
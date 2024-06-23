package com.insideout.usecase.feeling

import com.insideout.model.feeling.Feelings
import com.insideout.model.feeling.model.FeelingMemoryMarbleConnect
import com.insideout.usecase.feeling.port.FeelingSaver
import org.springframework.stereotype.Service

@Service
class UpdateFeelingsConnectMemoryMarbleService(
    private val feelingSaver: FeelingSaver,
) : UpdateFeelingsConnectMemoryMarbleUseCase {
    override fun execute(command: UpdateFeelingsConnectMemoryMarbleUseCase.Command): Feelings {
        val (memoryMarbleId, feelings) = command
        return feelingSaver.saveAll(
            feelings.map {
                when (val memoryMarbleConnect = it.memoryMarbleConnect) {
                    is FeelingMemoryMarbleConnect.DisConnectMemoryMarble -> {
                        it.updateMemoryMarbleConnect(memoryMarbleConnect.connect(memoryMarbleId))
                    }

                    is FeelingMemoryMarbleConnect.ConnectMemoryMarble -> it
                }
            }.let(::Feelings),
        )
    }
}

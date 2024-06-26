package com.insideout.usecase.feeling

import com.insideout.usecase.feeling.port.FeelingRemover
import org.springframework.stereotype.Service

@Service
class RemoveFeelingsService(
    private val feelingRemover: FeelingRemover,
) : RemoveFeelingsUseCase {
    override fun execute(command: RemoveFeelingsUseCase.Command) {
        feelingRemover.remove(command.feelings)
    }
}

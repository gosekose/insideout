package com.insideout.usecase.feeling

import com.insideout.model.feeling.Feelings
import com.insideout.usecase.feeling.port.FeelingReader
import org.springframework.stereotype.Service

@Service
class GetFeelingsService(
    private val feelingReader: FeelingReader,
) : GetFeelingsUseCase {
    override fun execute(query: GetFeelingsUseCase.Query): Feelings {
        return feelingReader.getByIds(query.ids)
    }
}
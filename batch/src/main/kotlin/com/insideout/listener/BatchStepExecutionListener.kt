package com.insideout.listener

import org.slf4j.LoggerFactory
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.StepExecutionListener

open class BatchStepExecutionListener : StepExecutionListener {
    private val logger = LoggerFactory.getLogger(BatchJobExecutionListener::class.java)

    override fun beforeStep(stepExecution: StepExecution) {
        stepExecution.executionContext.putLong("startTime", System.currentTimeMillis())
        addBeforeStep(stepExecution)
        super.beforeStep(stepExecution)
    }

    override fun afterStep(stepExecution: StepExecution): ExitStatus? {
        val startTime = stepExecution.executionContext.getLong("startTime")
        val endTime = System.currentTimeMillis()
        logger.info(
            """
            ======================================================================  
            Step ${stepExecution.stepName} executed in ${endTime - startTime} ms
            ======================================================================
            """.trimIndent(),
        )
        addAfterStep(stepExecution)
        return super.afterStep(stepExecution)
    }

    open fun addBeforeStep(stepExecution: StepExecution) {
        return
    }

    open fun addAfterStep(stepExecution: StepExecution) {
        return
    }
}

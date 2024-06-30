package com.insideout.job.dailyToPermanentUpdater

import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.item.ItemStreamException
import org.springframework.batch.item.database.AbstractPagingItemReader
import org.springframework.batch.item.database.JdbcPagingItemReader
import org.springframework.batch.item.database.PagingQueryProvider
import org.springframework.beans.factory.InitializingBean
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.util.ClassUtils
import java.sql.ResultSet
import java.sql.SQLException
import java.util.SortedMap
import java.util.TreeMap
import java.util.concurrent.CopyOnWriteArrayList
import javax.sql.DataSource

open class CustomJdbcPagingItemReader<T>(
    private var dataSource: DataSource,
    private var namedParameterJdbcTemplate: NamedParameterJdbcTemplate? = null,
    private var rowMapper: RowMapper<T>,
    private var queryProvider: PagingQueryProvider,
    private var parameterValues: Map<String, Any>? = null,
    private var fetchSize: Int = VALUE_NOT_SET,
) : AbstractPagingItemReader<T>(), InitializingBean {
    init {
        name = ClassUtils.getShortName(JdbcPagingItemReader::class.java)
    }

    private lateinit var firstPageSql: String
    private lateinit var remainingPagesSql: String
    private var startAfterValues: MutableMap<String, Any>? = null
    private var previousStartAfterValues: MutableMap<String, Any>? = null
    private val failedValues = mutableMapOf<String, String>()

    /**
     * Check mandatory properties.
     * @see org.springframework.beans.factory.InitializingBean.afterPropertiesSet
     */
    @Throws(Exception::class)
    override fun afterPropertiesSet() {
        super.afterPropertiesSet()
        val jdbcTemplate = JdbcTemplate(dataSource)
        if (fetchSize != VALUE_NOT_SET) {
            jdbcTemplate.fetchSize = fetchSize
        }
        jdbcTemplate.maxRows = pageSize
        namedParameterJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)
        queryProvider.init(dataSource)
        firstPageSql = queryProvider.generateFirstPageQuery(pageSize)
        remainingPagesSql = queryProvider.generateRemainingPagesQuery(pageSize)
    }

    override fun doReadPage() {
        results = results?.apply { clear() } ?: CopyOnWriteArrayList()

        val rowCallback = PagingRowMapper()
        val query: List<T> =
            try {
                when {
                    page == 0 -> {
                        logger.info("SQL used for reading first page: [$firstPageSql]")
                        executeQuery(firstPageSql, rowCallback, parameterValues)
                    }

                    startAfterValues != null -> {
                        previousStartAfterValues = startAfterValues
                        logger.info("SQL used for reading remaining pages: [$remainingPagesSql]")
                        executeQuery(remainingPagesSql, rowCallback, startAfterValues)
                    }

                    else -> emptyList()
                }
            } catch (e: Exception) {
                failedValues["page_$page"] = firstPageSql
                logger.error("Error occurred while reading page: ", e)
                retryWithPage(page, 1, 1, rowCallback)
            }
        results.addAll(query)
    }

    private fun executeQuery(
        sql: String,
        rowCallback: PagingRowMapper,
        parameters: Map<String, Any>?,
    ): List<T> {
        return if (!parameters.isNullOrEmpty()) {
            if (queryProvider.isUsingNamedParameters) {
                namedParameterJdbcTemplate?.query(sql, getParameterMap(parameters, null), rowCallback) ?: emptyList()
            } else {
                getJdbcTemplate().query(sql, rowCallback, *getParameterList(parameters, null).toTypedArray())
            }
        } else {
            getJdbcTemplate().query(sql, rowCallback)
        }
    }

    private fun retryWithPage(
        page: Int,
        offsetCount: Int,
        retryCount: Int,
        rowCallback: PagingRowMapper,
    ): List<T> {
        val adjustedMinValue = pageSize * offsetCount
        val sqlWithOffset = "$firstPageSql OFFSET $adjustedMinValue"

        logger.info("Retry SQL used for reading page $page with adjusted offset: [$sqlWithOffset]")

        return try {
            executeQuery(sqlWithOffset, rowCallback, parameterValues)
        } catch (e: Exception) {
            logger.error("Error occurred while retrying page: ", e)
            if (retryCount < 5) {
                failedValues["page_$page"] = firstPageSql
                retryWithPage(page + 1, offsetCount + 1, retryCount + 1, rowCallback)
            } else {
                throw e
            }
        }
    }

    @Throws(ItemStreamException::class)
    override fun update(executionContext: ExecutionContext) {
        super.update(executionContext)
        if (isSaveState) {
            if (isAtEndOfPage() && startAfterValues != null) {
                executionContext.put(getExecutionContextKey(START_AFTER_VALUE), startAfterValues)
            } else if (previousStartAfterValues != null) {
                executionContext.put(getExecutionContextKey(START_AFTER_VALUE), previousStartAfterValues)
            }
        }
        if (failedValues.isNotEmpty()) {
            executionContext.put(getExecutionContextKey(FAIL_VALUE), failedValues)
        }
    }

    private fun isAtEndOfPage(): Boolean {
        return currentItemCount % pageSize == 0
    }

    override fun open(executionContext: ExecutionContext) {
        if (isSaveState) {
            startAfterValues = executionContext[getExecutionContextKey(START_AFTER_VALUE)] as MutableMap<String, Any>?
            if (startAfterValues == null) {
                startAfterValues = LinkedHashMap()
            }
        }
        super.open(executionContext)
    }

    private fun getParameterMap(
        values: Map<String, Any>?,
        sortKeyValues: Map<String, Any>?,
    ): Map<String, Any> {
        val parameterMap: MutableMap<String, Any> = LinkedHashMap()
        if (values != null) {
            parameterMap.putAll(values)
        }
        if (!sortKeyValues.isNullOrEmpty()) {
            for ((key, value) in sortKeyValues) {
                parameterMap["_$key"] = value
            }
        }
        if (logger.isDebugEnabled) {
            logger.debug("Using parameterMap:$parameterMap")
        }
        return parameterMap
    }

    private fun getParameterList(
        values: Map<String, Any>?,
        sortKeyValue: Map<String, Any>?,
    ): List<Any> {
        val sm: SortedMap<String, Any> = TreeMap()
        if (values != null) {
            sm.putAll(values)
        }
        val parameterList: MutableList<Any> = ArrayList()
        parameterList.addAll(sm.values)
        if (!sortKeyValue.isNullOrEmpty()) {
            val keys: List<Map.Entry<String, Any>> = ArrayList(sortKeyValue.entries)
            for (i in keys.indices) {
                for (j in 0 until i) {
                    parameterList.add(keys[j].value)
                }
                parameterList.add(keys[i].value)
            }
        }
        logger.info("Using parameterList:$parameterList")
        return parameterList
    }

    private inner class PagingRowMapper : RowMapper<T> {
        @Throws(SQLException::class)
        override fun mapRow(
            rs: ResultSet,
            rowNum: Int,
        ): T {
            if (startAfterValues == null) {
                startAfterValues = LinkedHashMap()
            }
            for ((key) in queryProvider.sortKeys.entries) {
                startAfterValues!![key] = rs.getObject(key)
            }
            return rowMapper.mapRow(rs, rowNum) as T
        }
    }

    private fun getJdbcTemplate(): JdbcTemplate {
        return namedParameterJdbcTemplate!!.jdbcOperations as JdbcTemplate
    }

    companion object {
        const val START_AFTER_VALUE = "start.after"
        const val FAIL_VALUE = "fail.value"
        const val VALUE_NOT_SET = -1
    }
}

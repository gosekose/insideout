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
        if (results == null) {
            results = CopyOnWriteArrayList()
        } else {
            results.clear()
        }

        val rowCallback = PagingRowMapper()
        val query: List<T>

        if (page == 0) {
            if (logger.isDebugEnabled) {
                logger.debug("SQL used for reading first page: [$firstPageSql]")
            }
            query = if (!parameterValues.isNullOrEmpty()) {
                if (this.queryProvider.isUsingNamedParameters) {
                    namedParameterJdbcTemplate!!.query(
                        firstPageSql, getParameterMap(parameterValues, null),
                        rowCallback
                    )
                } else {
                    getJdbcTemplate().query(
                        firstPageSql, rowCallback,
                        *getParameterList(parameterValues, null).toTypedArray()
                    )
                }
            } else {
                getJdbcTemplate().query(firstPageSql, rowCallback)
            }
        } else if (startAfterValues != null) {
            previousStartAfterValues = startAfterValues
            if (logger.isDebugEnabled) {
                logger.debug("SQL used for reading remaining pages: [$remainingPagesSql]")
            }

            query = if (this.queryProvider.isUsingNamedParameters) {
                namedParameterJdbcTemplate!!.query(
                    remainingPagesSql,
                    getParameterMap(parameterValues, startAfterValues), rowCallback
                )
            } else {
                getJdbcTemplate().query(
                    remainingPagesSql, rowCallback,
                    *getParameterList(parameterValues, startAfterValues).toTypedArray()
                )
            }
        } else { // page != 0, startAfterValues == null
            // Adjust minValue (offset) to skip failed chunk
            val adjustedMinValue = (page - 1) * pageSize
            // Generate SQL with adjusted offset
            val sqlWithOffset = "${firstPageSql} OFFSET ${adjustedMinValue} ROWS FETCH NEXT ${pageSize} ROWS ONLY"

            if (logger.isDebugEnabled) {
                logger.debug("SQL used for reading page $page with adjusted offset: [$sqlWithOffset]")
            }

            query = if (!parameterValues.isNullOrEmpty()) {
                if (this.queryProvider.isUsingNamedParameters) {
                    namedParameterJdbcTemplate!!.query(
                        sqlWithOffset, getParameterMap(parameterValues, null),
                        rowCallback
                    )
                } else {
                    getJdbcTemplate().query(
                        sqlWithOffset, rowCallback,
                        *getParameterList(parameterValues, null).toTypedArray()
                    )
                }
            } else {
                getJdbcTemplate().query(sqlWithOffset, rowCallback)
            }
        }
        results.addAll(query)
    }

    @Throws(ItemStreamException::class)
    override fun update(executionContext: ExecutionContext) {
        super.update(executionContext)
        if (isSaveState) {
            if (isAtEndOfPage() && startAfterValues != null) {
                // restart on next page
                executionContext.put(getExecutionContextKey(START_AFTER_VALUE), startAfterValues)
            } else if (previousStartAfterValues != null) {
                // restart on current page
                executionContext.put(getExecutionContextKey(START_AFTER_VALUE), previousStartAfterValues)
            }
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

    private fun getParameterMap(values: Map<String, Any>?, sortKeyValues: Map<String, Any>?): Map<String, Any> {
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

    private fun getParameterList(values: Map<String, Any>?, sortKeyValue: Map<String, Any>?): List<Any> {
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
        if (logger.isDebugEnabled) {
            logger.debug("Using parameterList:$parameterList")
        }
        return parameterList
    }


    private inner class PagingRowMapper : RowMapper<T> {
        @Throws(SQLException::class)
        override fun mapRow(rs: ResultSet, rowNum: Int): T {
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

        const val VALUE_NOT_SET = -1
    }
}
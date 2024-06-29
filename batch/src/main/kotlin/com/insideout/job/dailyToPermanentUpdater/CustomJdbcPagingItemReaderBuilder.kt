package com.insideout.job.dailyToPermanentUpdater

import org.springframework.batch.item.database.Order
import org.springframework.batch.item.database.PagingQueryProvider
import org.springframework.batch.item.database.support.Db2PagingQueryProvider
import org.springframework.batch.item.database.support.DerbyPagingQueryProvider
import org.springframework.batch.item.database.support.H2PagingQueryProvider
import org.springframework.batch.item.database.support.HanaPagingQueryProvider
import org.springframework.batch.item.database.support.HsqlPagingQueryProvider
import org.springframework.batch.item.database.support.MariaDBPagingQueryProvider
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider
import org.springframework.batch.item.database.support.OraclePagingQueryProvider
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider
import org.springframework.batch.item.database.support.SqlServerPagingQueryProvider
import org.springframework.batch.item.database.support.SqlitePagingQueryProvider
import org.springframework.batch.item.database.support.SybasePagingQueryProvider
import org.springframework.batch.support.DatabaseType
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.MetaDataAccessException
import org.springframework.util.Assert
import javax.sql.DataSource

class CustomJdbcPagingItemReaderBuilder<T>(
    private val dataSource: DataSource,
) {
    private var fetchSize = CustomJdbcPagingItemReader.VALUE_NOT_SET
    private var queryProvider: PagingQueryProvider? = null
    private var parameterValues: Map<String, Any>? = null
    private var pageSize = 10
    private var groupClause: String? = null
    private var selectClause: String? = null
    private var fromClause: String? = null
    private var whereClause: String? = null
    private var saveState = true
    private var name: String? = null
    private var maxItemCount = Int.MAX_VALUE
    private var currentItemCount = 0

    private lateinit var sortKeys: Map<String, Order>
    private lateinit var rowMapper: RowMapper<T>

    fun saveState(saveState: Boolean): CustomJdbcPagingItemReaderBuilder<T> {
        this.saveState = saveState
        return this
    }

    fun name(name: String?): CustomJdbcPagingItemReaderBuilder<T> {
        this.name = name
        return this
    }

    fun maxItemCount(maxItemCount: Int): CustomJdbcPagingItemReaderBuilder<T> {
        this.maxItemCount = maxItemCount
        return this
    }

    fun currentItemCount(currentItemCount: Int): CustomJdbcPagingItemReaderBuilder<T> {
        this.currentItemCount = currentItemCount
        return this
    }

    fun fetchSize(fetchSize: Int): CustomJdbcPagingItemReaderBuilder<T> {
        this.fetchSize = fetchSize
        return this
    }

    fun rowMapper(rowMapper: RowMapper<T>): CustomJdbcPagingItemReaderBuilder<T> {
        this.rowMapper = rowMapper
        return this
    }

    fun beanRowMapper(mappedClass: Class<T>?): CustomJdbcPagingItemReaderBuilder<T> {
        rowMapper = mappedClass?.let { BeanPropertyRowMapper(it) }!!
        return this
    }

    fun parameterValues(parameterValues: Map<String, Any>?): CustomJdbcPagingItemReaderBuilder<T> {
        this.parameterValues = parameterValues
        return this
    }

    fun pageSize(pageSize: Int): CustomJdbcPagingItemReaderBuilder<T> {
        this.pageSize = pageSize
        return this
    }

    fun groupClause(groupClause: String?): CustomJdbcPagingItemReaderBuilder<T> {
        this.groupClause = groupClause
        return this
    }

    fun selectClause(selectClause: String?): CustomJdbcPagingItemReaderBuilder<T> {
        this.selectClause = selectClause
        return this
    }

    fun fromClause(fromClause: String?): CustomJdbcPagingItemReaderBuilder<T> {
        this.fromClause = fromClause
        return this
    }

    fun whereClause(whereClause: String?): CustomJdbcPagingItemReaderBuilder<T> {
        this.whereClause = whereClause
        return this
    }

    fun sortKeys(sortKeys: Map<String, Order>): CustomJdbcPagingItemReaderBuilder<T> {
        this.sortKeys = sortKeys
        return this
    }

    fun queryProvider(provider: PagingQueryProvider?): CustomJdbcPagingItemReaderBuilder<T> {
        queryProvider = provider
        return this
    }


    fun build(): CustomJdbcPagingItemReader<T> {
        Assert.isTrue(pageSize > 0, "pageSize must be greater than zero")
        Assert.notNull(dataSource, "dataSource is required")

        if (saveState) {
            Assert.hasText(name, "A name is required when saveState is set to true")
        }

        val readerProvider = if (queryProvider == null) {
            Assert.hasLength(selectClause, "selectClause is required when not providing a PagingQueryProvider")
            Assert.hasLength(fromClause, "fromClause is required when not providing a PagingQueryProvider")
            Assert.notEmpty(sortKeys, "sortKeys are required when not providing a PagingQueryProvider")
            determineQueryProvider(dataSource)
        } else {
            queryProvider!!
        }

        val reader: CustomJdbcPagingItemReader<T> = CustomJdbcPagingItemReader(
            dataSource = dataSource,
            fetchSize = fetchSize,
            parameterValues = parameterValues,
            queryProvider = readerProvider,
            rowMapper = rowMapper,
        )

        reader.setMaxItemCount(maxItemCount)
        reader.currentItemCount = currentItemCount
        reader.name = name
        reader.isSaveState = saveState

        reader.pageSize = pageSize
        return reader
    }

    private fun determineQueryProvider(dataSource: DataSource): PagingQueryProvider {
        return try {
            val databaseType = DatabaseType.fromMetaData(dataSource)
            val provider = when (databaseType) {
                DatabaseType.DERBY -> DerbyPagingQueryProvider()
                DatabaseType.DB2, DatabaseType.DB2VSE, DatabaseType.DB2ZOS, DatabaseType.DB2AS400 -> Db2PagingQueryProvider()
                DatabaseType.H2 -> H2PagingQueryProvider()
                DatabaseType.HANA -> HanaPagingQueryProvider()
                DatabaseType.HSQL -> HsqlPagingQueryProvider()
                DatabaseType.SQLSERVER -> SqlServerPagingQueryProvider()
                DatabaseType.MYSQL -> MySqlPagingQueryProvider()
                DatabaseType.MARIADB -> MariaDBPagingQueryProvider()
                DatabaseType.ORACLE -> OraclePagingQueryProvider()
                DatabaseType.POSTGRES -> PostgresPagingQueryProvider()
                DatabaseType.SYBASE -> SybasePagingQueryProvider()
                DatabaseType.SQLITE -> SqlitePagingQueryProvider()
            }
            provider.setSelectClause(selectClause)
            provider.setFromClause(fromClause)
            provider.setWhereClause(whereClause)
            provider.groupClause = groupClause
            provider.sortKeys = sortKeys
            provider
        } catch (e: MetaDataAccessException) {
            throw IllegalArgumentException("Unable to determine PagingQueryProvider type", e)
        }
    }
}
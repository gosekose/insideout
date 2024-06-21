package com.insideout.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class ListLongToStringConverter : AttributeConverter<List<Long>, String> {
    override fun convertToDatabaseColumn(attribute: List<Long>?): String {
        return if (attribute.isNullOrEmpty()) {
            ""
        } else {
            attribute.joinToString(separator = ",")
        }
    }

    override fun convertToEntityAttribute(dbData: String?): List<Long> {
        return if (dbData.isNullOrEmpty()) {
            emptyList()
        } else {
            dbData.split(",").map { it.toLong() }
        }
    }
}

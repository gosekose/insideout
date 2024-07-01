package com.insideout.converter

import com.google.gson.reflect.TypeToken
import com.insideout.extension.GSON
import com.insideout.extension.fromJsonToType
import com.insideout.model.memoryMarble.model.MemoryMarbleContent
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.lang.reflect.Type

@Converter
class ListFileContentToStringConverter : AttributeConverter<List<MemoryMarbleContent.FileContent>, String> {
    private val type: Type = object : TypeToken<List<MemoryMarbleContent.FileContent>>() {}.type

    override fun convertToDatabaseColumn(attribute: List<MemoryMarbleContent.FileContent>?): String {
        return if (attribute.isNullOrEmpty()) {
            "[]"
        } else {
            GSON.toJson(attribute, type)
        }
    }

    override fun convertToEntityAttribute(dbData: String?): List<MemoryMarbleContent.FileContent> {
        return if (dbData.isNullOrEmpty()) {
            emptyList()
        } else {
            GSON.fromJsonToType(dbData, type) ?: emptyList()
        }
    }
}

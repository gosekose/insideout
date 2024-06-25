package com.insideout.converter

import com.insideout.model.memory.type.StoreType
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class StringToStoreTypeConverter : Converter<String, StoreType> {
    override fun convert(source: String): StoreType? {
        return try {
            StoreType.valueOf(source.uppercase())
        } catch (e: Exception) {
            null
        }
    }
}

package com.insideout.model.page

import com.insideout.model.BaseDomainModel

class Pagination<T : BaseDomainModel>(
    val content: List<T>,
    val hasNext: Boolean,
)

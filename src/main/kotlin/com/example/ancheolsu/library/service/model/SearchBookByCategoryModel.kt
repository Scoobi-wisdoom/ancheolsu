package com.example.ancheolsu.library.service.model

import org.springframework.data.domain.Pageable

data class SearchBookByCategoryModel(
    val categoryName: String,
    val pageable: Pageable,
)

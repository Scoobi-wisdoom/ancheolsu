package com.example.ancheolsu.library.service.model

import org.springframework.data.domain.Pageable

data class SearchBookModel(
    val title: String?,
    val author: String?,
    val pageable: Pageable,
)

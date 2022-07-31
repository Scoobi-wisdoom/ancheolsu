package com.example.ancheolsu.library.controller.response

data class SearchBookByCategoryResponse(
    val categories: List<String>,
    val author: String,
    val title: String,
    val borrowable: Boolean,
)

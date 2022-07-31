package com.example.ancheolsu.library.service.model

data class SearchBookResultModel(
    val categories: List<String>,
    val author: String,
    val title: String,
    val borrowable: Boolean,
)

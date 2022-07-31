package com.example.ancheolsu.library.controller.response

import com.example.ancheolsu.library.service.model.SearchBookResultModel

data class SearchBookResponse(
    val categories: List<String>,
    val author: String,
    val title: String,
    val borrowable: Boolean,
) {
    companion object {
        fun from(model: SearchBookResultModel): SearchBookResponse {
            return SearchBookResponse(
                categories = model.categories,
                author = model.author,
                title = model.title,
                borrowable = model.borrowable,
            )
        }
    }
}

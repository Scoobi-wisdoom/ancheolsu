package com.example.ancheolsu.library.controller.request

import com.example.ancheolsu.library.service.model.RegisterBookModel

data class RegisterBookRequest(
    val title: String,
    val author: String,
    val categoryNames: Set<String>,
) {
    fun toModel(): RegisterBookModel {
        return RegisterBookModel(
            title = title,
            author = author,
            categoryNames = categoryNames
        )
    }
}

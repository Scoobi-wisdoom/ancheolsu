package com.example.ancheolsu.library.controller.request

import com.example.ancheolsu.library.service.model.RegisterBookModel

data class RegisterBookRequest(
    val title: String,
    val author: String,
    val category: String,
) {
    fun toModel(): RegisterBookModel {
        return RegisterBookModel(
            title = title,
            author = author,
            category = category
        )
    }
}

package com.example.ancheolsu.library.service.model

import com.example.ancheolsu.book.entity.Book

data class RegisterBookModel(
    val title: String,
    val author: String,
    val category: String,
) {
    fun toEntity(): Book {
        return Book(
            author = author,
            title = title,
            borrowable = true,
        )
    }
}

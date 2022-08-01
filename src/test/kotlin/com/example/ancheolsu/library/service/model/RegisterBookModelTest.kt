package com.example.ancheolsu.library.service.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class RegisterBookModelTest {
    @Test
    fun toEntity() {
        val title = "title"
        val author = "author"
        val category = "category"

        val model = RegisterBookModel(
            title = title,
            author = author,
            categoryNames = setOf(category)
        )
        val entity = model.toEntity()

        assertAll(
            { assertTrue(entity.title == model.title) },
            { assertTrue(entity.author == model.author) },
            { assertTrue(entity.borrowable) },
        )
    }
}

package com.example.ancheolsu.bookcategoryrelation.entity

import com.example.ancheolsu.book.entity.Book
import com.example.ancheolsu.category.entity.Category
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class BookCategoryRelationTest {
    @Test
    fun from() {
        val bookCategoryRelation = BookCategoryRelation.from(
            book = Book(id = 0, author = "", title = "", borrowable = false),
            categories = listOf(Category(id = 1, categoryName = ""))
        )
        assertAll(
            { assertTrue(bookCategoryRelation[0].bookId == 0L) },
            { assertTrue(bookCategoryRelation[0].categoryId == 1L) },
        )
    }
}

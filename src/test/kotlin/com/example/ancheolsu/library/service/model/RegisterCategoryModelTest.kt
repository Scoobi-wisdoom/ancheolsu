package com.example.ancheolsu.library.service.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class RegisterCategoryModelTest {
    @Test
    fun toEntity() {
        val model = RegisterCategoryModel(
            categoryName = "categoryName"
        )

        val entity = model.toEntity()
        assertTrue(entity.categoryName == model.categoryName)
    }
}

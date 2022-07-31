package com.example.ancheolsu.library.service.model

import com.example.ancheolsu.category.entity.Category

data class RegisterCategoryModel(
    val categoryName: String,
) {
    fun toEntity(): Category {
        return Category(
            categoryName = categoryName
        )
    }
}

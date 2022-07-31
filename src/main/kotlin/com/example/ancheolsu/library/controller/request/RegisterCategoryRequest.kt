package com.example.ancheolsu.library.controller.request

import com.example.ancheolsu.library.service.model.RegisterCategoryModel

data class RegisterCategoryRequest(
    val categoryName: String,
) {
    fun toModel(): RegisterCategoryModel {
        return RegisterCategoryModel(
            categoryName = categoryName,
        )
    }
}

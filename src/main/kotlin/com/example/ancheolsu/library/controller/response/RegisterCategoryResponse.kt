package com.example.ancheolsu.library.controller.response

import com.example.ancheolsu.library.service.model.RegisterCategoryResultModel

data class RegisterCategoryResponse(
    val categoryId: Long,
) {
    companion object {
        fun from(model: RegisterCategoryResultModel): RegisterCategoryResponse {
            return RegisterCategoryResponse(
                categoryId = model.categoryId
            )
        }
    }
}

package com.example.ancheolsu.library.controller.response

import com.example.ancheolsu.library.service.model.RegisterBookResultModel

data class RegisterBookResponse(
    val bookId: Long,
) {
    companion object {
        fun from(model: RegisterBookResultModel): RegisterBookResponse {
            return RegisterBookResponse(bookId = model.bookId)
        }
    }
}

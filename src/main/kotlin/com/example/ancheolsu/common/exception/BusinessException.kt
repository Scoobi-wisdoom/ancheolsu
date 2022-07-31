package com.example.ancheolsu.common.exception

import org.springframework.http.HttpStatus

class BusinessException(
    override val message: String,
    cause: Throwable?,
    val httpStatus: HttpStatus,
) : RuntimeException(
    message,
    cause,
) {
}

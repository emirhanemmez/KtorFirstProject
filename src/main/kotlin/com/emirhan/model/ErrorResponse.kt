package com.emirhan.model

data class ErrorResponse(
    val status: Int,
    val exception: Throwable
)

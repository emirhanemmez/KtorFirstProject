package com.emirhan.model

import kotlinx.serialization.Serializable

data class ErrorResponse(
    val status: Int,
    val exception: Throwable
)

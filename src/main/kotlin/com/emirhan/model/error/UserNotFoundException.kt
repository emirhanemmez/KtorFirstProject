package com.emirhan.model.error

data class UserNotFoundException(
    override val message: String?,
    override val cause: Throwable? = null,
) : Throwable(message, cause)

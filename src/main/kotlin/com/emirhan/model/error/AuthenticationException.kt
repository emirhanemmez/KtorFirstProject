package com.emirhan.model.error

data class AuthenticationException(
        override val message: String?,
        override val cause: Throwable? = null
): Throwable(message, cause)
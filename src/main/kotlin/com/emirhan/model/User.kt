package com.emirhan.model

import com.papsign.ktor.openapigen.annotations.Response
import kotlinx.serialization.Serializable

@Serializable
@Response
data class User(
        val id: Int,
        val username: String,
        val password: String
)

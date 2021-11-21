package com.emirhan.model

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class UserRequest(
    val username: String,
    var password: String
) {
    fun hashedPassword(): String = BCrypt.hashpw(password, BCrypt.gensalt())
}

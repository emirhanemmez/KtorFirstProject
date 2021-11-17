package com.emirhan.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.emirhan.model.User
import io.ktor.config.*

class TokenManager(val config : HoconApplicationConfig) {

    fun generateJWTToken(user: User) {
        val audience = config.property("audience").getString()
        val secret = config.property("secret").getString()
        val issuer = config.property("issuer").getString()
        val expirationDate = System.currentTimeMillis() + 60000

        val token = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("username", user.username)
            .withClaim("id", user.id)
            .sign(Algorithm.HMAC512(secret))
    }
}
package com.emirhan.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.emirhan.model.User
import io.ktor.config.HoconApplicationConfig
import java.util.Date

class TokenManager(private val config: HoconApplicationConfig) {

    private val audience = config.property("ktor.security.jwt.audience").getString()
    private val secret = config.property("ktor.security.jwt.secret").getString()
    private val issuer = config.property("ktor.security.jwt.issuer").getString()
    private val algorithm = Algorithm.HMAC512(secret)
    private val expirationDate = System.currentTimeMillis() + 60000

    fun generateJWTToken(user: User): String =
            JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("username", user.username)
                    .withClaim("id", user.id)
                    .withExpiresAt(Date(expirationDate))
                    .sign(algorithm)

}
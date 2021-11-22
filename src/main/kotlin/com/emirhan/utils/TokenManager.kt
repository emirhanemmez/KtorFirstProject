package com.emirhan.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.emirhan.model.User
import io.ktor.config.*
import java.util.Date

class TokenManager(val config: HoconApplicationConfig) {

    private val audience = config.property("jwt.audience").getString()
    private val secret = config.property("jwt.secret").getString()
    private val issuer = config.property("jwt.issuer").getString()
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
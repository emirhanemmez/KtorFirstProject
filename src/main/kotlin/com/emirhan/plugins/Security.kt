package com.emirhan.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.emirhan.utils.TokenManager
import io.ktor.auth.*
import io.ktor.util.*
import io.ktor.application.*
import io.ktor.auth.jwt.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*

fun Application.configureSecurity() {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = environment.config.property("jwt.realm").getString()

            verifier(JWT.require(Algorithm.HMAC512(environment.config.property("jwt.secret").getString()))
                    .withIssuer(environment.config.property("jwt.issuer").getString())
                    .build())

            validate { credential ->
                if (!credential.payload.getClaim("username").asString().isNullOrEmpty()) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}

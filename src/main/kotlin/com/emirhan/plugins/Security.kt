package com.emirhan.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.emirhan.model.error.AuthenticationException
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.config.*
import io.ktor.features.*

fun Application.configureSecurity() {
    install(Authentication) {
        jwt("auth-jwt") {
            val config = HoconApplicationConfig(ConfigFactory.load())
            realm = config.property("ktor.security.jwt.realm").getString()

            verifier(
                JWT.require(Algorithm.HMAC512(config.property("ktor.security.jwt.secret").getString()))
                    .withIssuer(config.property("ktor.security.jwt.issuer").getString())
                    .build()
            )

            validate { credential ->
                if (!credential.payload.getClaim("username").asString().isNullOrEmpty()) {
                    JWTPrincipal(credential.payload)
                } else {
                    throw AuthenticationException("Authentication failed!")
                }
            }

            challenge { _, _ ->
                call.request.headers["Authorization"]?.let {
                    if (it.isNotEmpty()) {
                        throw AuthenticationException("Token Expired!")
                    } else {
                        throw BadRequestException("Authorization header can not be blank!")
                    }
                } ?: throw BadRequestException("Authorization header can not be blank!")
            }
        }
    }
}

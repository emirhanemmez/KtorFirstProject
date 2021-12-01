package com.emirhan.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.emirhan.model.error.AuthenticationException
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.features.BadRequestException

fun Application.configureSecurity() {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = environment.config.property("ktor.security.jwt.realm").getString()

            verifier(JWT.require(Algorithm.HMAC512(environment.config.property("ktor.security.jwt.secret").getString()))
                    .withIssuer(environment.config.property("ktor.security.jwt.issuer").getString())
                    .build())

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

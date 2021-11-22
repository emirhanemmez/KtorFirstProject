package com.emirhan.plugins

import com.emirhan.model.error.AuthenticationException
import com.emirhan.model.error.UserNotFoundException
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.BadRequestException
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

fun Application.errorHandling() {
    install(StatusPages) {
        exception<AuthenticationException> { cause ->
            call.respond(status = HttpStatusCode.Unauthorized, message = cause.message ?: "Authentication failed!")
        }
        exception<UserNotFoundException> { cause ->
            call.respond(status = HttpStatusCode.NotFound, message = cause.message ?: "Not found!")
        }
        exception<BadRequestException> { cause ->
            call.respond(status = HttpStatusCode.BadRequest, message = cause.message ?: "Bad request!")
        }
    }
}
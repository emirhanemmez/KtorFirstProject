package com.emirhan.plugins

import com.emirhan.model.error.AuthenticationException
import com.emirhan.model.error.UserNotFoundException
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*

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
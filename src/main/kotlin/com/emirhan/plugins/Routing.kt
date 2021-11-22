package com.emirhan.plugins

import io.ktor.application.Application
import io.ktor.routing.routing

fun Application.configureRouting() {

    routing {
        userRouting()
    }
}

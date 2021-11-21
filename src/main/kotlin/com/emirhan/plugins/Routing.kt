package com.emirhan.plugins

import io.ktor.routing.*
import io.ktor.application.*

fun Application.configureRouting() {

    routing {
        userRouting()
    }
}

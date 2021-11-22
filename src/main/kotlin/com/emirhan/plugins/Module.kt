package com.emirhan.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.serialization.*

fun Application.module() {
    install(ContentNegotiation) {
        json()
        gson()
    }
}
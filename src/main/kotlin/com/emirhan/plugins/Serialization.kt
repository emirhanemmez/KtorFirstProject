package com.emirhan.plugins

import com.fasterxml.jackson.annotation.JsonInclude
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.serialization.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        jackson {
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
            // (You can add additional Jackson config stuff here, such as registerModules(JavaTimeModule()), etc.)
        }
        json()
    }
}

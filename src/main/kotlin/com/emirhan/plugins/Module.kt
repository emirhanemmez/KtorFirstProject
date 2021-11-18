package com.emirhan.plugins

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.serialization.*
import org.jetbrains.exposed.sql.Database

fun Application.module() {
    install(ContentNegotiation) {
        json()
        gson()
    }
    /*
    install(Authentication) {
        jwt {

        }
    }
     */

    Database.connect(
        url = "jdbc:postgresql://0.0.0.0:5433/postgres",
        driver = "org.postgresql.Driver",
        user = "emirhanemmez",
        password = "emmez1453"
    )
}
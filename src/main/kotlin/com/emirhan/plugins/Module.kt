package com.emirhan.plugins

import com.emirhan.database.UserTable
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.serialization.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

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
}
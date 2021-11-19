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

    /*Database.connect(
        url = "jdbc:postgresql://ec2-99-81-177-233.eu-west-1.compute.amazonaws.com:5432/da5qc3vrrl1rp7",
        driver = "org.postgresql.Driver",
        user = "ytkhaljqplgtns",
        password = "67033d4a658b9ad6865164972319712a0290fb11253556be9733d424c23d1f76"
    )*/

    Database.connect(
        url = "jdbc:postgresql://localhost:5433/postgres",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "emmez1453"
    )
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(UserTable)
    }
}
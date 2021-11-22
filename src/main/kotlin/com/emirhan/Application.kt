package com.emirhan

import com.emirhan.database.UserTable
import com.emirhan.plugins.configureRouting
import com.emirhan.plugins.configureSecurity
import com.emirhan.plugins.errorHandling
import com.typesafe.config.ConfigFactory
import io.ktor.application.install
import io.ktor.config.HoconApplicationConfig
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.serialization.json
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    embeddedServer(Netty, environment = applicationEngineEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load())

        val dbHost = config.property("ktor.deployment.db_host").getString()
        val dbPort = config.property("ktor.deployment.db_port").getString().toInt()
        val dbUser = config.property("ktor.deployment.db_user").getString()
        val dbPassword = config.property("ktor.deployment.db_password").getString()

        module {
            install(ContentNegotiation) {
                json()
                gson()
            }
            errorHandling()
            configureSecurity()
            configureRouting()

            Database.connect(
                    url = "jdbc:postgresql://$dbHost:$dbPort/postgres",
                    driver = "org.postgresql.Driver",
                    user = dbUser,
                    password = dbPassword
            )
            transaction {
                addLogger(StdOutSqlLogger)
                SchemaUtils.create(UserTable)
            }
        }
        connector {
            port = config.property("ktor.deployment.port").getString().toInt()
            host = config.property("ktor.deployment.host").getString()
        }
    }).start(wait = true)
}

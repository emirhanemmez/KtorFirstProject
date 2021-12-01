package com.emirhan

import com.emirhan.database.UserTable
import com.emirhan.di.diModule
import com.emirhan.plugins.configureOpenApi
import com.emirhan.plugins.configureRouting
import com.emirhan.plugins.configureSecurity
import com.emirhan.plugins.configureSerialization
import com.emirhan.plugins.errorHandling
import com.typesafe.config.ConfigFactory
import io.ktor.config.*
import io.ktor.network.tls.certificates.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

fun main() {
    embeddedServer(Netty, environment = applicationEngineEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load())

        val serverHost = config.property("ktor.deployment.host").getString()
        val serverPort = config.property("ktor.deployment.port").getString().toInt()
        val serverSSLPort = config.property("ktor.deployment.sslPort").getString().toInt()

        val dbHost = config.property("ktor.deployment.db_host").getString()
        val dbPort = config.property("ktor.deployment.db_port").getString().toInt()
        val dbUser = config.property("ktor.deployment.db_user").getString()
        val dbPassword = config.property("ktor.deployment.db_password").getString()

        val keyAlias = config.property("ktor.security.ssl.keyAlias").getString()
        val keyPassword = config.property("ktor.security.ssl.keyPassword").getString()
        val jksPassword = config.property("ktor.security.ssl.jksPassword").getString()
        val keyStoreFile = File("build/keystore.jks")

        val keyStore = generateCertificate(
            file = keyStoreFile,
            keyAlias = keyAlias,
            keyPassword = keyPassword,
            jksPassword = jksPassword
        )

        module {
            configureSerialization()
            errorHandling()
            configureSecurity()
            diModule()
            configureRouting()
            configureOpenApi()

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
            port = serverPort
            host = serverHost
        }

        sslConnector(
            keyStore = keyStore,
            keyAlias = keyAlias,
            keyStorePassword = { keyPassword.toCharArray() },
            privateKeyPassword = { jksPassword.toCharArray() },
        ) {
            port = serverSSLPort
            keyStorePath = keyStoreFile
            host = serverHost
        }
    }).start(wait = true)
}

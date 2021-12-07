package com.emirhan

import com.emirhan.controller.UserController
import com.emirhan.controller.UserDataSourceImp
import com.emirhan.di.diModule
import com.emirhan.plugins.configureOpenApi
import com.emirhan.plugins.configureRouting
import com.emirhan.plugins.configureSecurity
import com.emirhan.plugins.configureSerialization
import com.emirhan.plugins.errorHandling
import com.emirhan.plugins.initDatabase
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.features.*
import io.ktor.network.tls.certificates.*
import io.ktor.request.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.core.context.startKoin
import org.mpierce.ktor.csrf.CsrfProtection
import org.mpierce.ktor.csrf.OriginMatchesHostHeader
import org.mpierce.ktor.csrf.OriginMatchesKnownHost
import org.slf4j.event.Level
import java.io.File

fun main() {
    embeddedServer(Netty, environment = applicationEngineEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load())

        val serverHost = config.property("ktor.deployment.host").getString()
        val serverPort = config.property("ktor.deployment.port").getString().toInt()
        val serverSSLPort = config.property("ktor.deployment.sslPort").getString().toInt()

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
        module(Application::module)
    }).start(wait = true)
}

fun Application.module() {
    configureSerialization()
    errorHandling()
    configureSecurity()
    diModule()
    configureRouting()
    configureOpenApi()
    initDatabase()
    install(CallLogging) {
        level = Level.INFO
        format {
            val status = it.response.status()
            val httpMethod = it.request.httpMethod.value
            val userAgent = it.request.userAgent()
            "Status: $status, HTTP method: $httpMethod, User Agent: $userAgent"
        }
        filter { call -> call.request.path().startsWith("/") }
    }

    // If you are using cookies you should have csrf protection
    install(CsrfProtection) {
        applyToAllRoutes()

        val config = HoconApplicationConfig(ConfigFactory.load())
        val host = config.property("ktor.deployment.host").getString()

        validate(OriginMatchesKnownHost("http", host = host))
        validate(OriginMatchesKnownHost("https", host = host))
    }
}

fun Application.testModule() {
    configureSecurity()
    configureRouting()
    configureSerialization()
    initDatabase()
    install(CallLogging) {
        level = Level.INFO
        format {
            val status = it.response.status()
            val httpMethod = it.request.httpMethod.value
            "Status: $status, HTTP method: $httpMethod"
        }
        filter { call -> call.request.path().startsWith("/") }
    }
    startKoin {
        modules(
            org.koin.dsl.module(createdAtStart = true) {
                single { UserDataSourceImp() }
                single { UserController(get()) }
            }
        )
    }
}

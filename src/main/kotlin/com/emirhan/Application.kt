package com.emirhan

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.emirhan.plugins.*
import org.jetbrains.exposed.sql.Database

fun main() {
    embeddedServer(Netty, port = 8080, host = "localhost") {
        configureRouting()
        /*configureMonitoring()
        configureSerialization()
        configureSecurity()*/
        module()
    }.start(wait = true)
}

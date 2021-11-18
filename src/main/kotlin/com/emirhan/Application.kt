package com.emirhan

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.emirhan.plugins.*
import org.jetbrains.exposed.sql.Database

fun main() {
    embeddedServer(Netty, port = 5433, host = "0.0.0.0") {
        configureRouting()
        /*configureMonitoring()
        configureSerialization()
        configureSecurity()*/
        module()
    }.start(wait = true)
}

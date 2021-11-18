package com.emirhan

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.emirhan.plugins.*
import org.jetbrains.exposed.sql.Database

fun main() {
    embeddedServer(Netty, port = 5432, host = "ec2-99-81-177-233.eu-west-1.compute.amazonaws.com") {
        configureRouting()
        /*configureMonitoring()
        configureSerialization()
        configureSecurity()*/
        module()
    }.start(wait = true)
}

package com.emirhan.plugins

import com.papsign.ktor.openapigen.openAPIGen
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureRouting() {

    routing {
        userRouting()
        get("/openapi.json") {
            call.respond(application.openAPIGen.api.serialize())
        }
        get("/") {
            call.respondRedirect("/swagger-ui/index.html?url=/openapi.json", true)
        }
        get("/hello") {
            call.respondText(text = "Hello", status = HttpStatusCode.OK)
        }
    }
}

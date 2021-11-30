package com.emirhan.plugins

import com.emirhan.controller.UserController
import com.emirhan.model.User
import com.papsign.ktor.openapigen.APITag
import com.papsign.ktor.openapigen.OpenAPIGen
import com.papsign.ktor.openapigen.route.*
import com.papsign.ktor.openapigen.route.path.normal.get
import com.papsign.ktor.openapigen.route.response.respond
import io.ktor.application.*
import io.ktor.http.*
import org.koin.ktor.ext.inject

fun Application.configureOpenApi() {
    val userController by inject<UserController>()

    install(OpenAPIGen) {
        serveSwaggerUi = true
        swaggerUiPath = "/swagger-ui"
        info {
            version = "0.0.1"
            title = "FirstKtor Tutorial API"
            description = "The FirstKtor Tutorial API"
        }

        server("http://localhost:8080/") {
            description = "Local Server"
        }
    }

    apiRouting {
        tag(object : APITag {
            override val description: String
                get() = "User Methods"
            override val name: String
                get() = "User"

        }) {
            route("/user") {
                get<Unit, List<User>>(
                    info(
                        summary = "Get all users",
                        description = "Return a list of all users"
                    ),
                    status(HttpStatusCode.OK),
                    example = listOf(User(1,"emir", "1234"), User(2,"emirhan", "5678")),
                ) {
                    respond(userController.getAllUsers())
                }
            }
        }
    }
}
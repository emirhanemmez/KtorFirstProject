package com.emirhan.plugins

import com.emirhan.database.UserTable
import com.emirhan.model.UserRequest
import com.emirhan.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.principal
import io.ktor.config.HoconApplicationConfig
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.mindrot.jbcrypt.BCrypt
import javax.security.sasl.AuthenticationException

fun Route.userRouting() {

    val tokenManager = TokenManager(HoconApplicationConfig(ConfigFactory.load()))

    authenticate("auth-jwt") {
        route("/user") {
            get {
                val userList = UserTable.getAllUsers()
                if (userList.isNotEmpty())
                    call.respond(userList)
                else
                    call.respondText("No user found")
            }
            get("/{id}") {
                val id = call.parameters["id"]?.toInt()
                id?.let {
                    val user = UserTable.getUserById(it)
                    if (user != null)
                        call.respond(user)
                    else
                        call.respondText("User not found", status = HttpStatusCode.NotFound)
                }

            }
            put {
                val user = call.receiveOrNull<UserRequest>()
                user?.let {
                    user.password = user.hashedPassword()
                    UserTable.addUser(user)
                    call.respondText("User saved successfully", status = HttpStatusCode.Created)
                }
            }
            delete("/{id}") {
                val id = call.parameters["id"]?.toInt()
                id?.let {
                    UserTable.deleteUser(it)
                    call.respondText("User deleted", status = HttpStatusCode.OK)
                }
            }
        }

        get("/jwtExpirationTime") {
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("username").asString()
            val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
            call.respondText(text = "Hello, $username ! Token is expired at $expiresAt")
        }
    }

    route("/login") {
        post {
            val userRequest = call.receive<UserRequest>()
            val user = UserTable.getUserByUsername(userRequest.username)

            if (user != null)
                call.respond(hashMapOf("token" to tokenManager.generateJWTToken(user)))
        }
    }
}
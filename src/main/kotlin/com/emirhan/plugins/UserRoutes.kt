package com.emirhan.plugins

import com.emirhan.database.UserTable
import com.emirhan.model.UserRequest
import com.emirhan.model.error.AuthenticationException
import com.emirhan.model.error.UserNotFoundException
import com.emirhan.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mindrot.jbcrypt.BCrypt
import java.io.File

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
                        call.respond(Json.encodeToString(user))
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

            intercept(ApplicationCallPipeline.Features) {
                println(call.request.headers.entries())
            }
        }

        get("/jwtExpirationTime") {
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("username").asString()
            val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
            call.respondText(text = "Hello, $username ! Token is expired at $expiresAt")
        }
    }

    route("/signUp") {
        put {
            val user = call.receiveOrNull<UserRequest>()
            user?.let {
                user.password = user.hashedPassword()
                UserTable.addUser(user)
                call.respondText("User saved successfully", status = HttpStatusCode.Created)
            }
        }
    }

    // download file
    route("/fileDownloadAsAttachment") {
        get {

            val file = File("files/kedi.jpg")
            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment.withParameter(
                    ContentDisposition.Parameters.FileName, "downloadedKediImage.jpg"
                ).toString()
            )

            call.respondFile(file)
        }
    }

    // download file and show in browser
    route("/fileDownloadAsInline") {
        get {

            val file = File("files/kedi.jpg")
            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Inline.withParameter(
                    ContentDisposition.Parameters.FileName, "downloadedKediImage.jpg"
                ).toString()
            )

            call.respondFile(file)
        }
    }

    route("/staticContent") {
        static {
            defaultResource("kedi.jpg", "files")
        }
    }

    route("/login") {
        post {
            val userRequest = call.receive<UserRequest>()
            val user = UserTable.getUserByUsername(userRequest.username)

            if (user != null) {
                if (BCrypt.checkpw(userRequest.password, user.password))
                    call.respond(tokenManager.generateJWTToken(user))
                else {
                    throw AuthenticationException("Wrong username or password!")
                }
            } else {
                throw UserNotFoundException("User not found!")
            }
        }
    }
}
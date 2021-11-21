package com.emirhan.plugins

import com.emirhan.database.UserTable
import com.emirhan.model.UserRequest
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.mindrot.jbcrypt.BCrypt
import javax.security.sasl.AuthenticationException

fun Route.userRouting() {
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
    route("/login") {
        post {
            val userRequest = call.receive<UserRequest>()
            val user = UserTable.getUserByUsername(userRequest.username) ?: throw NotFoundException()
            val doesPasswordMatch = BCrypt.checkpw(userRequest.password, user.password)
            if (doesPasswordMatch) {
                call.respond(status = HttpStatusCode.OK, message = "Success")
            } else {
                call.respondText("Auth Error", status = HttpStatusCode.Unauthorized)
            }
        }
    }
}
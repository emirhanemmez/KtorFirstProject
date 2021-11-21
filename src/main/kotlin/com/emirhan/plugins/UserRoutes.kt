package com.emirhan.plugins

import com.emirhan.database.UserTable
import com.emirhan.model.UserRequest
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

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
                UserTable.addUser(user)
                call.respondText("User saved successfully", status = HttpStatusCode.OK)
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
}
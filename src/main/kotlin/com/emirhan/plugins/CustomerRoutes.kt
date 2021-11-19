package com.emirhan.plugins

import com.emirhan.database.UserTable
import com.emirhan.model.Customer
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

private val customerList = mutableListOf<Customer>().apply {
    add(Customer(id = "0", firstName = "Emirhan", lastName = "Emmez", email = "emirhan.emmez@loodos.com"))
    add(Customer(id = "1", firstName = "İrem", lastName = "Emmez", email = "iremcil@gmail.com"))
}

fun Route.customerRouting() {
    route("/customer") {
        get {
            if (customerList.isEmpty()){
                call.respond(status = HttpStatusCode.NotFound, message = "No customers found")
            } else {
                call.respond(customerList)
            }
        }
        get("/{id}") {
            val id = call.parameters["id"]
            // val customer = customerList.find { it.id == id }
            val user = UserTable.getUserById(id?.toInt()!!)
            call.respond(user)

        }
        post {
            val customer = call.receive<Customer>()
            customerList.add(customer)
            call.respondText("Customer stored correctly", status = HttpStatusCode.OK)
        }
        delete("{id}") {

        }
    }
}
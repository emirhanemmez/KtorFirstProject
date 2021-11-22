package com.emirhan.database

import com.emirhan.model.User
import com.emirhan.model.UserRequest
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.SQLException

object UserTable : Table("user") {
    private val id = integer("id").autoIncrement()
    private val username = varchar("username", 20)
    private val password = text("password")

    override val primaryKey = PrimaryKey(id)

    fun addUser(user: UserRequest) = transaction {
        try {
            UserTable.insert {
                it[username] = user.username
                it[password] = user.password
            }
        } catch (e: SQLException) {
            println(e.message)
        }
    }

    fun getAllUsers(): MutableList<User> {
        val list = mutableListOf<User>()
        transaction {
            try {
                list.addAll(UserTable.selectAll().map {
                    it.toUser()
                }.toList())
            } catch (e: SQLException) {
                println(e.message)
            }
        }
        return list
    }

    fun getUserById(id: Int): User? {
        var user: User? = null
        transaction {
            try {
                UserTable.select {
                    UserTable.id eq id
                }.map {
                    user = it.toUser()
                }
            } catch (e: SQLException) {
                println(e.message)
            }
        }
        return user
    }

    fun getUserByUsername(username: String): User? {
        var user: User? = null
        transaction {
            try {
                UserTable.select {
                    UserTable.username eq username
                }.map {
                    user = it.toUser()
                }
            } catch (e: SQLException) {
                println(e.message)
            }
        }
        return user
    }

    fun deleteUser(id: Int) = transaction {
        try {
            UserTable.deleteWhere { UserTable.id eq id }
        } catch (e: SQLException) {
            println(e.message)
        }
    }

    private fun ResultRow.toUser() = User(
            id = this[id],
            username = this[username],
            password = this[password]
    )
}
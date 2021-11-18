package com.emirhan.database

import com.emirhan.model.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.SQLException

object UserTable: Table() {
    private val id = integer("id").autoIncrement()
    private val username = varchar("username", 20)
    private val password = varchar("password", 20)

    override val primaryKey = PrimaryKey(id)

    fun getUserById(id: Int) = transaction {
        try {
            UserTable.select {
                UserTable.id eq id
            }.map {
                it.toUser()
            }
        } catch (e: SQLException) {
            print(e.message)
        }
    }

    private fun ResultRow.toUser() = User(
        id = this[id],
        username = this[username],
        password = this[password]
    )
}
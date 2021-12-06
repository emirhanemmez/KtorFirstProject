package com.emirhan.plugins

import com.emirhan.database.UserTable
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.initDatabase() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    
    val dbHost = config.property("ktor.deployment.db_host").getString()
    val dbPort = config.property("ktor.deployment.db_port").getString().toInt()
    val dbUser = config.property("ktor.deployment.db_user").getString()
    val dbPassword = config.property("ktor.deployment.db_password").getString()

    Database.connect(
        url = "jdbc:postgresql://$dbHost:$dbPort/postgres",
        driver = "org.postgresql.Driver",
        user = dbUser,
        password = dbPassword
    )
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(UserTable)
    }
}
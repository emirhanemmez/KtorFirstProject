package com.emirhan.controller

import com.emirhan.database.UserTable
import com.emirhan.model.User
import com.emirhan.model.error.AuthenticationException
import com.emirhan.model.error.UserNotFoundException
import com.emirhan.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.response.*
import org.mindrot.jbcrypt.BCrypt

class UserDataSourceImp : UserDataSource {
    val tokenManager = TokenManager(HoconApplicationConfig(ConfigFactory.load()))
    override suspend fun login(username: String, password: String): String {
        val user = UserTable.getUserByUsername(username)

        if (user != null) {
            if (user.password == BCrypt.hashpw(password, BCrypt.gensalt()))
                return tokenManager.generateJWTToken(user)
            else {
                throw AuthenticationException("Wrong username or password!")
            }
        } else {
            throw UserNotFoundException("User not found!")
        }
    }

    override suspend fun getAllUsers(): List<User> = UserTable.getAllUsers()

    override suspend fun getUserById(id: Int): User? = UserTable.getUserById(id)
}
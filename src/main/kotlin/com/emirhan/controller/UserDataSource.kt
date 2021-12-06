package com.emirhan.controller

import com.emirhan.model.User

interface UserDataSource {
    suspend fun login(username: String, password: String): String
    suspend fun getAllUsers(): List<User>
    suspend fun getUserById(id: Int): User?
}
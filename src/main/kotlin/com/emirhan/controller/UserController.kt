package com.emirhan.controller

import com.emirhan.model.User

class UserController(private val userDataSourceImp: UserDataSourceImp) {
    suspend fun login(userName: String, password: String) = userDataSourceImp.login(userName, password)

    suspend fun getAllUsers(): List<User> = userDataSourceImp.getAllUsers()

    suspend fun getUserById(id: Int): User? = userDataSourceImp.getUserById(id)
}
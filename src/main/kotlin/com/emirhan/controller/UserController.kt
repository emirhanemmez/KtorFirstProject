package com.emirhan.controller

import com.emirhan.model.User

class UserController(private val userDataSourceImp: UserDataSourceImp) {
    suspend fun getAllUsers(): List<User> = userDataSourceImp.getAllUsers()
}
package com.emirhan.controller

import com.emirhan.model.User

interface UserDataSource {
    suspend fun getAllUsers(): List<User>
}
package com.emirhan.controller

import com.emirhan.database.UserTable
import com.emirhan.model.User

class UserDataSourceImp(): UserDataSource {
    override suspend fun getAllUsers(): List<User> {
        return UserTable.getAllUsers()
    }
}
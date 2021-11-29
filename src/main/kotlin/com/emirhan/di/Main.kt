package com.emirhan.di

import com.emirhan.controller.UserController
import com.emirhan.controller.UserDataSource
import com.emirhan.controller.UserDataSourceImp
import io.ktor.application.*
import org.koin.dsl.module

fun Application.diModule() {
    val module = module {
        single<UserDataSource> { UserDataSourceImp() }
        single { UserController(get()) }
    }
}
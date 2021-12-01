package com.emirhan.di

import com.emirhan.controller.UserController
import com.emirhan.controller.UserDataSourceImp
import io.ktor.application.*
import org.koin.core.logger.Level
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.koin.logger.slf4jLogger

fun Application.diModule() {
    install(Koin) {
        slf4jLogger(Level.DEBUG)
        modules(appModule)
    }
}

val appModule = module(createdAtStart = true) {
    single { UserDataSourceImp() }
    single { UserController(get()) }
}
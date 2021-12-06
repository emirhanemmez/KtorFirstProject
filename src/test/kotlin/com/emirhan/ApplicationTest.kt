package com.emirhan

import com.emirhan.model.UserRequest
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun testRoot(): Unit = withTestApplication({ testModule() }) {
        runBlocking {
            val token = login(this@withTestApplication)
            handleRequest(HttpMethod.Get, "/user/1") {
                addHeader(HttpHeaders.Authorization, "Bearer $token")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    private suspend fun login(engine: TestApplicationEngine): String? {
        engine.handleRequest(HttpMethod.Post, "/login") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
            setBody(Json.encodeToString(UserRequest("emirhan", "1234")))
            //setBody(listOf("username" to "emirhan", "password" to "1234").formUrlEncode())
        }.apply {
            return response.content
        }
    }

    @AfterTest
    fun finish() {
        stopKoin()
    }
}
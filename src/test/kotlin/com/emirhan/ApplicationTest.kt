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
    fun testRoot(): Unit = withBaseTestApplication {
        runBlocking {
            handleRequestWithToken(HttpMethod.Get, "/user/1") {
                addHeader(HttpHeaders.UserAgent, "EMIRHAN-PC")
            }.apply {
                assertEquals("EMIRHAN-PC", request.headers[HttpHeaders.UserAgent])
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @AfterTest
    fun finish() {
        stopKoin()
    }
}

fun <T> withBaseTestApplication(test: TestApplicationEngine.() -> T) {
    withTestApplication {
        application.testModule()
    }
}

fun TestApplicationEngine.handleRequestWithToken(
    method: HttpMethod,
    uri: String,
    setup: TestApplicationRequest.() -> Unit = {}
): TestApplicationCall {
    val token = handleRequest(HttpMethod.Post, "/login") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
        setBody(Json.encodeToString(UserRequest("emirhan", "1234")))
    }.response.content

    val requestSetup: TestApplicationRequest.() -> Unit = {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
        addHeader(HttpHeaders.Authorization, "Bearer $token")
        setup.invoke(this)
    }
    return handleRequest(method, uri, requestSetup)
}
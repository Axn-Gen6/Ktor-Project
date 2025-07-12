package com.example

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import kotlin.test.*

fun Application.testModule() {
    install(ContentNegotiation) {
        json()
    }
    configureRouting()
}

class AppointmentApiTest {

    @Test
    fun testAppointmentCreationAndDoubleBookingPrevention() = testApplication {
        application {
            testModule()
        }

        val createServiceResponse = client.post("/services") {
            contentType(ContentType.Application.Json)
            setBody(
                """
                {
                    "id": 1,
                    "name": "Haircut",
                    "description": "Fast haircut",
                    "defaultDurationInMinutes": 30
                }
                """.trimIndent()
            )
        }
        assertEquals(HttpStatusCode.Created, createServiceResponse.status)

        val response1 = client.post("/appointments") {
            contentType(ContentType.Application.Json)
            setBody(
                """
                {
                    "id": 1,
                    "clientName": "John",
                    "clientEmail": "john@example.com",
                    "appointmentTime": "2025-07-15T10:00:00",
                    "serviceId": 1
                }
                """.trimIndent()
            )
        }
        assertEquals(HttpStatusCode.Created, response1.status)

        val response2 = client.post("/appointments") {
            contentType(ContentType.Application.Json)
            setBody(
                """
                {
                    "id": 2,
                    "clientName": "Jane",
                    "clientEmail": "jane@example.com",
                    "appointmentTime": "2025-07-15T10:00:00",
                    "serviceId": 1
                }
                """.trimIndent()
            )
        }
        assertEquals(HttpStatusCode.Conflict, response2.status)
        assertEquals("Double booking detected!", response2.bodyAsText())
    }
}

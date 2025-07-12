package com.example

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class Service(
    val id: Int,
    val name: String,
    val description: String,
    val defaultDurationInMinutes: Int
)

@Serializable
data class Appointment(
    val id: Int,
    val clientName: String,
    val clientEmail: String,
    val appointmentTime: String,
    val serviceId: Int
)

val services = mutableListOf<Service>()
val appointments = mutableListOf<Appointment>()
fun Application.configureRouting() {
    routing {
        route("/services") {
            post {
                val service = call.receive<Service>()
                services.add(service)
                call.respond(HttpStatusCode.Created, service)
            }
            get {
                call.respond(services)
            }
        }

        route("/appointments") {
            post {
                val appointment = call.receive<Appointment>()
                val service = services.find { it.id == appointment.serviceId }
                if (service == null) {
                    call.respond(HttpStatusCode.NotFound, "Service not found.")
                    return@post
                }

                val isDoubleBooked = appointments.any {
                    it.serviceId == appointment.serviceId && it.appointmentTime == appointment.appointmentTime
                }
                if (isDoubleBooked) {
                    call.respond(HttpStatusCode.Conflict, "Double booking detected!")
                } else {
                    appointments.add(appointment)
                    call.respond(HttpStatusCode.Created, appointment)
                }
            }

            get {
                call.respond(appointments)
            }
        }
    }
}

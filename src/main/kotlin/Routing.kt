package com.example

import com.example.model.*
import com.example.repository.BookingRepository
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.http.*

fun Application.configureRouting() {
    routing {

        // ---- Services ----
        route("/services") {
            get {
                call.respond(BookingRepository.getAllServices())
            }

            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val service = id?.let { BookingRepository.getServiceById(it) }
                if (service != null) {
                    call.respond(service)
                } else call.respond(HttpStatusCode.NotFound, "Service not found")
            }

            post {
                val service = call.receive<Service>()
                BookingRepository.addService(service)
                call.respond(HttpStatusCode.Created, service)
            }

            put("{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val service = call.receive<Service>()
                if (id != null && BookingRepository.updateService(id, service)) {
                    call.respond(HttpStatusCode.OK, service)
                } else call.respond(HttpStatusCode.NotFound, "Service not found")
            }

            delete("{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id != null && BookingRepository.deleteService(id)) {
                    call.respond(HttpStatusCode.NoContent)
                } else call.respond(HttpStatusCode.NotFound, "Service not found")
            }
        }

        // ---- Appointments ----
        route("/appointments") {
            get {
                call.respond(BookingRepository.getAllAppointments())
            }

            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val appt = id?.let { BookingRepository.getAppointmentById(it) }
                if (appt != null) {
                    call.respond(appt)
                } else call.respond(HttpStatusCode.NotFound, "Appointment not found")
            }

            post {
                val appt = call.receive<Appointment>()
                val result = BookingRepository.addAppointment(appt)
                if (result) {
                    call.respond(HttpStatusCode.Created, appt)
                } else {
                    call.respond(HttpStatusCode.Conflict, "Double booking detected!")
                }
            }

            put("{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val appt = call.receive<Appointment>()
                if (id != null && BookingRepository.updateAppointment(id, appt)) {
                    call.respond(HttpStatusCode.OK, appt)
                } else call.respond(HttpStatusCode.NotFound, "Appointment not found")
            }

            delete("{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id != null && BookingRepository.deleteAppointment(id)) {
                    call.respond(HttpStatusCode.NoContent)
                } else call.respond(HttpStatusCode.NotFound, "Appointment not found")
            }
        }
    }
}

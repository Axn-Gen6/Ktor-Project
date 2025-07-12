package com.example.repository

import com.example.model.Appointment
import com.example.model.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object BookingRepository {
    private val services = mutableListOf<Service>()
    private val appointments = mutableListOf<Appointment>()

    private val formatter = DateTimeFormatter.ISO_DATE_TIME

    fun getAllServices() = services
    fun getServiceById(id: Int) = services.find { it.id == id }
    fun addService(service: Service) = services.add(service)
    fun updateService(id: Int, updated: Service): Boolean {
        val index = services.indexOfFirst { it.id == id }
        return if (index != -1) {
            services[index] = updated
            true
        } else false
    }
    fun deleteService(id: Int) = services.removeIf { it.id == id }

    fun getAllAppointments() = appointments
    fun getAppointmentById(id: Int) = appointments.find { it.id == id }

    fun addAppointment(appointment: Appointment): Boolean {
        val requestedTime = LocalDateTime.parse(appointment.appointmentTime, formatter)
        val service = services.find { it.id == appointment.serviceId } ?: return false

        val isDoubleBooked = appointments.any {
            it.serviceId == appointment.serviceId &&
                    LocalDateTime.parse(it.appointmentTime, formatter) == requestedTime
        }

        if (isDoubleBooked) return false

        appointments.add(appointment)
        return true
    }

    fun updateAppointment(id: Int, updated: Appointment): Boolean {
        val index = appointments.indexOfFirst { it.id == id }
        return if (index != -1) {
            appointments[index] = updated
            true
        } else false
    }

    fun deleteAppointment(id: Int) = appointments.removeIf { it.id == id }
}

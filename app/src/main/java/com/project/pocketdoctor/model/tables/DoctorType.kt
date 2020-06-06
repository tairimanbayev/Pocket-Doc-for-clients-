package com.project.pocketdoctor.model.tables

data class DoctorType (
    val type: String
) {
    companion object {
        private val doctorRoles = hashMapOf(
            "ENT" to DoctorType("Лор"),
            "therapist" to DoctorType("Терапевт"),
            "pediatrician" to DoctorType("Педиатр"),
            "neuropathologist" to DoctorType("Невропатолог"),
            "procedures" to DoctorType("Процедуры"),
            "analysis" to DoctorType("Анализы"),
            "massage" to DoctorType("Массаж")
        )

        fun getDoctorRole(key: String?) = doctorRoles[key]?.type ?: key ?: ""

        fun getDoctorTag(role: String) =
            doctorRoles.keys.firstOrNull { doctorRoles[it] == DoctorType(role) } ?: role

        fun getDoctorList() = doctorRoles.values.toList()
    }
}


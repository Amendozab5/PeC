package com.example.pec

import kotlinx.serialization.Serializable

@Serializable
data class Materia(
    val id: Long,
    val nombre: String,
    val nivel: Int
)

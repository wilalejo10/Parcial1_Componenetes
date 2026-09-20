package com.example.parcial_1

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hallazgos")
data class Hallazgo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val casoId: Int,
    val tipo: String,
    val descripcion: String,
    val fecha: String
)
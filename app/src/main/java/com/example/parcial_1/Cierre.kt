package com.example.parcial_1

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cierres")
data class Cierre(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val casoId: Int,
    val fechaCierre: String,
    val motivo: String
)
package com.example.parcial_1

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "casos")
data class Caso(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val descripcion: String,
    val fecha: String,
    val estado: String = "Abierto",
   val fechaCierre: String? = null
)
package com.example.parcial_1

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CasoDao {

    @Insert
    suspend fun insertarCaso(caso: Caso)

    @Query("SELECT * FROM casos")
    suspend fun obtenerCasos(): List<Caso>

    @Query("SELECT * FROM casos WHERE id = :id")
    suspend fun obtenerCasoPorId(id: Int): Caso?
}
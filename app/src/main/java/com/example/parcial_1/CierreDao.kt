package com.example.parcial_1

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CierreDao {

    @Insert
    suspend fun insertarCierre(cierre: Cierre)

    @Query("SELECT * FROM cierres WHERE casoId = :casoId LIMIT 1")
    suspend fun obtenerCierrePorCaso(casoId: Int): Cierre?
}
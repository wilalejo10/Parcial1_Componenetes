package com.example.parcial_1

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CasoDao {

    @Insert
    suspend fun insertarCaso(caso: Caso)

    @Update
    suspend fun actualizarCaso(caso: Caso)

    @Delete
    suspend fun eliminarCaso(caso: Caso)

    @Query("SELECT * FROM casos")
    suspend fun obtenerCasos(): List<Caso>

    @Query("SELECT * FROM casos WHERE id = :id")
    suspend fun obtenerCasoPorId(id: Int): Caso?

    @Query("SELECT * FROM casos WHERE titulo LIKE '%' || :query || '%' OR descripcion LIKE '%' || :query || '%'")
    suspend fun buscarCasos(query: String): List<Caso>
}
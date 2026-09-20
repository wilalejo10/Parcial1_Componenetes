package com.example.parcial_1

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HallazgoDao {

    @Insert
    suspend fun insertarHallazgo(hallazgo: Hallazgo)

    @Delete
    suspend fun eliminarHallazgo(hallazgo: Hallazgo)

    @Query("SELECT * FROM hallazgos WHERE casoId = :casoId ORDER BY id DESC")
    suspend fun obtenerHallazgosPorCaso(casoId: Int): List<Hallazgo>

    @Query("SELECT * FROM hallazgos WHERE casoId = :casoId AND tipo = :tipo ORDER BY id DESC")
    suspend fun obtenerPorTipo(casoId: Int, tipo: String): List<Hallazgo>
}
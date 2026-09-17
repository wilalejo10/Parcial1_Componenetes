package com.example.parcial_1

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface CasoDao {

    @Insert
    suspend fun insertarCaso(caso: Caso)
}
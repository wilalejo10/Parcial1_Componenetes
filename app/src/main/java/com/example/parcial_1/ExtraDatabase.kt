package com.example.parcial_1

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Hallazgo::class, Cierre::class],
    version = 1,
    exportSchema = false
)
abstract class ExtraDatabase : RoomDatabase() {

    abstract fun hallazgoDao(): HallazgoDao
    abstract fun cierreDao(): CierreDao

    companion object {

        @Volatile
        private var INSTANCE: ExtraDatabase? = null

        fun getDatabase(context: Context): ExtraDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExtraDatabase::class.java,
                    "extra_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
package com.fiap18Mob.clean.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fiap18Mob.clean.model.User

@Database(entities = arrayOf(User::class), version = 1)
public abstract class CleanRoomDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: CleanRoomDatabase? = null

        fun getDatabase(context: Context): CleanRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CleanRoomDatabase::class.java,
                    "clean_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
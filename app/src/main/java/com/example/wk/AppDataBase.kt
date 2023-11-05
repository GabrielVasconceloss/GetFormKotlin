package com.example.wk

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Form::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun formDao(): FormDao

}
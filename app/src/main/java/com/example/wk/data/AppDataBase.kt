package com.example.wk.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Form::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun formDao(): FormDao

}
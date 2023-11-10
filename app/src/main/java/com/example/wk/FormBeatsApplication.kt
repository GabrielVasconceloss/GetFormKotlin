package com.example.wk

import android.app.Application
import androidx.room.Room
import com.example.wk.data.AppDataBase


class FormBeatsApplication: Application() {
    private lateinit var dataBase: AppDataBase
    override fun onCreate() {
        super.onCreate()
        dataBase = Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java, "getform-database"
        ).build()
    }

    fun getAppDatabase(): AppDataBase{
        return dataBase
    }
}
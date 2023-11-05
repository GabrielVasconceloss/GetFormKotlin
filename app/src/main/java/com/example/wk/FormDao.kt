package com.example.wk

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface FormDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(form: Form)
}
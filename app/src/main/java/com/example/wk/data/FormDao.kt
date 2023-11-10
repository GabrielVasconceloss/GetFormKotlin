package com.example.wk.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FormDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(form: Form)
    @Query("SELECT * FROM form")
    fun getAll(): LiveData<List<Form>>
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(form: Form)
    @Query("DELETE FROM form WHERE id =:id")
    fun delete(id: Int)
}
package com.example.gbymap.domain.marks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MarksDao {

    @Query("SELECT * FROM MarksEntity")
    suspend fun getAll(): List<MarksEntity>

    @Insert
    suspend fun insert(mark: MarksEntity)

    @Update
    suspend fun update(mark: MarksEntity)

    @Delete
    suspend fun delete(mark: MarksEntity)
}
package com.example.gbymap.domain.marks

interface MarksDatabaseHelper {
    suspend fun getAll(): List<MarksEntity>
    suspend fun insert(mark: MarksEntity)
    suspend fun update(mark: MarksEntity)
    suspend fun delete(mark: MarksEntity)
}
package com.example.gbymap.domain.marks

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MarksEntity::class], version = 1, exportSchema = true)
abstract class MarksDatabase : RoomDatabase() {
    abstract fun marksDao(): MarksDao
}
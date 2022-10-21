package com.example.gbymap.domain.marks

import android.content.Context
import androidx.room.Room

object MarksDatabaseBuilder {
    private var instance: MarksDatabase? = null

    fun getInstance(context: Context): MarksDatabase {
        if (instance == null) {
            synchronized(MarksDatabase::class) {
                instance = buildRoomDB(context)
            }
        }
        return instance!!
    }

    private fun buildRoomDB(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            MarksDatabase::class.java,
            "marksDB"
        ).build()
}
package com.example.gbymap.data.marks

import com.example.gbymap.domain.marks.MarksDatabase
import com.example.gbymap.domain.marks.MarksDatabaseHelper
import com.example.gbymap.domain.marks.MarksEntity

class MarksDatabaseHelperImpl(private val marksDatabase: MarksDatabase) : MarksDatabaseHelper {
    override suspend fun getAll(): List<MarksEntity> {
        return marksDatabase.marksDao().getAll()
    }

    override suspend fun insert(mark: MarksEntity) {
        marksDatabase.marksDao().insert(mark)
    }

    override suspend fun update(mark: MarksEntity) {
        marksDatabase.marksDao().update(mark)
    }

    override suspend fun delete(mark: MarksEntity) {
        marksDatabase.marksDao().delete(mark)
    }
}
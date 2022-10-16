package com.example.gbymap.ui.mainScreen

import com.example.gbymap.domain.marks.MarksEntity

interface MainContract {

    interface ViewModel {
        fun getMarks()
        fun insertMark(mark: MarksEntity)
        fun updateMark(mark: MarksEntity)
        fun deleteMark(mark: MarksEntity)
        fun updateMarkHelper(mark: MarksEntity)
        fun updateMarkEnd()
    }
}
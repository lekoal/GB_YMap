package com.example.gbymap.ui.mainScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gbymap.domain.marks.MarksDatabaseHelper
import com.example.gbymap.domain.marks.MarksEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MarksViewModel(
    private val marksDBHelper: MarksDatabaseHelper
) : ViewModel(), MainContract.ViewModel {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val _marks = MutableLiveData<List<MarksEntity>>()

    override fun getMarks() {
        coroutineScope.launch {
            _marks.postValue(
                marksDBHelper.getAll()
            )
        }
    }

    var marks: LiveData<List<MarksEntity>> = _marks

    override fun insertMark(mark: MarksEntity) {
        coroutineScope.launch {
            marksDBHelper.insert(mark)
        }
    }

    override fun updateMark(mark: MarksEntity) {
        coroutineScope.launch {
            marksDBHelper.update(mark)
        }
    }

    override fun deleteMark(mark: MarksEntity) {
        coroutineScope.launch {
            marksDBHelper.delete(mark)
        }
    }

    override fun onCleared() {
        coroutineScope.cancel()
        super.onCleared()
    }
}
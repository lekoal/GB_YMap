package com.example.gbymap.domain.marks

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MarksEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String,
    var latitude: Double,
    var longitude: Double
)
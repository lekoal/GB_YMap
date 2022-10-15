package com.example.gbymap.utils

import com.example.gbymap.domain.marks.MarksEntity
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

class MapManager(private val mapView: MapView) {

    fun moveTo(lat: Double, lon: Double) {
        mapView.map.move(
            CameraPosition(
                Point(lat, lon),
                16.0f,
                0.0f,
                0.0f
            )
        )
    }

    fun addMark(lat: Double, lon: Double) {
        moveTo(lat, lon)
        mapView.map.mapObjects.addPlacemark(Point(lat, lon))
    }

    fun addListMarks(marks: List<MarksEntity>) {
        marks.forEach { mark ->
            mapView.map.mapObjects.addPlacemark(Point(
                mark.latitude,
                mark.longitude
            ))
        }
    }

    fun onStart() {
        mapView.onStart()
    }

    fun onStop() {
        mapView.onStop()
    }
}
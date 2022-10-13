package com.example.gbymap.utils

import com.yandex.mapkit.Animation
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
            ),
            Animation(Animation.Type.SMOOTH, 3f),
            null
        )
    }

    fun onStart() {
        mapView.onStart()
    }

    fun onStop() {
        mapView.onStop()
    }
}
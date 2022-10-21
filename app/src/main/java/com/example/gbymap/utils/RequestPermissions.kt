package com.example.gbymap.utils

import android.Manifest
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity

object RequestPermissions {
    fun location(activity: FragmentActivity) {
        val permissions = ArrayList<String>()
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        ActivityCompat.requestPermissions(
            activity,
            permissions.toTypedArray(),
            1
        )
    }
}
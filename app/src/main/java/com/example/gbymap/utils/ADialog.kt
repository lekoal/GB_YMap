package com.example.gbymap.utils

import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.example.gbymap.ui.LOCATION_ENABLE
import com.example.gbymap.ui.LOCATION_PERMISSION

class ADialog(private val activity: FragmentActivity) {
    fun show(
        alertType: String,
        title: String,
        message: String,
        positive: String? = null,
        negative: String? = null,
        neutral: String? = null
    ) {
        val aDialog = AlertDialog.Builder(activity)
            .setTitle(title)
            .setMessage(message)
        if (positive != null) aDialog.setPositiveButton(positive) { _, _ ->
            when (alertType) {
                LOCATION_PERMISSION -> {
                    RequestPermissions.location(activity)
                }
                LOCATION_ENABLE -> {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    activity.startActivity(intent)
                }
            }
        }
        if (negative != null) aDialog.setNegativeButton(negative) { dialog, _ ->
            dialog.dismiss()
        }
        if (neutral != null) aDialog.setNeutralButton(neutral) { _, _ ->
            //neutral function
        }
        aDialog.create()
        aDialog.show()
    }
}
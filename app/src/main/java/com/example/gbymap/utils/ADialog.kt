package com.example.gbymap.utils

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity

class ADialog(private val activity: FragmentActivity) {
    fun show(
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
            RequestPermissions.location(activity)
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
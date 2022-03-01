package com.example.nback.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface

object UiUtils {
    fun createDialog(activity: Activity?, title: String?, msg: String?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton(
            "Ok"
        ) { dialog: DialogInterface?, id: Int -> }
        return builder.create()
    }
}
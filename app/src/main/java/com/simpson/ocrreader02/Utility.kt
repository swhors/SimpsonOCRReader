package com.simpson.ocrreader02

import android.Manifest;
import android.annotation.SuppressLint
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class Utility {
    companion object {
        val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: Int = 123

        @SuppressLint("ObsoleteSdkInt")
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
        fun checkPermission(context: Context?): Boolean {
            val currentAPIVersion = Build.VERSION.SDK_INT
            Log.d(TAG, "API Version = $currentAPIVersion")
            Log.d(TAG, "API Version = ${Build.VERSION_CODES.M}")

            if (currentAPIVersion >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_MEDIA_IMAGES
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d(TAG, "check_permission = 0")
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            context as Activity,
                            Manifest.permission.READ_MEDIA_IMAGES
                        )
                    ) {
                        Log.d(TAG, "check_permission = 1")

                        val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
                        alertBuilder.setCancelable(true)
                        alertBuilder.setTitle("Permission necessary")
                        alertBuilder.setMessage("External storage permission is necessary")
                        alertBuilder.setPositiveButton(
                            "Yes",
                            object : DialogInterface.OnClickListener {
                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    ActivityCompat.requestPermissions(
                                        context,
                                        arrayOf<String?>(Manifest.permission.READ_MEDIA_IMAGES),
                                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                                    )
                                }
                            })
                        val alert: AlertDialog = alertBuilder.create()
                        alert.show()
                    } else {
                        Log.d(TAG, "check_permission = 2")

                        ActivityCompat.requestPermissions(
                            context,
                            arrayOf<String?>(Manifest.permission.READ_MEDIA_IMAGES),
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                        )
                    }
                    Log.d(TAG, "check_permission = 3")

                    return false
                } else {
                    Log.d(TAG, "check_permission = 4")

                    return true
                }
            } else {
                Log.d(TAG, "check_permission = 5")

                return true
            }
        }
    }
}
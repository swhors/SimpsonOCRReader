package com.simpson.ocrreader02

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream

class BmpUtil {
    companion object {
        fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
            val bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
            return bitmap
        }

        fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
            var outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream)
            return outputStream.toByteArray()
        }
        fun decodeUriToBitmap(context: Context?, uri: Uri, options: BitmapFactory.Options): Result<Bitmap> = runCatching {
            context!!.contentResolver.openInputStream(uri).use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, options)
                    ?: throw RuntimeException("bitmap decoding failed")
            }
        }

    }
}
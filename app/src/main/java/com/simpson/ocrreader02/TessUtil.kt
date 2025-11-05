package com.simpson.ocrreader02

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class TessUtil {
    companion object {
        fun checkLanguageFile(context: Context, dir: String): Boolean {
            val file = File(dir)
            if (!file.exists() && file.mkdirs()) createFiles(context, dir)
            else if (file.exists()) {
                val laguageList = listOf<String>("eng", "kor", "kor_vert", "jpn", "jpn_vert")
                for (lang in laguageList) {
                    val filePath = dir + "/$lang.traineddata"
                    val langDataFile = File(filePath)
                    if (!langDataFile.exists()) createFiles(context, dir)
                }
            }
            return true
        }

        private fun createFiles(context: Context, dir: String?) {
            val assetMgr = context.getAssets()

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                val laguageList = listOf<String>("eng", "kor", "kor_vert", "jpn", "jpn_vert")
                for (lang in laguageList) {
                    inputStream = assetMgr.open("$lang.traineddata")
                    val destFile = dir + "/$lang.traineddata"
                    outputStream = FileOutputStream(destFile)
                    val buffer = ByteArray(1024)
                    var read: Int
                    while ((inputStream.read(buffer).also { read = it }) != -1) {
                        outputStream.write(buffer, 0, read)
                    }
                    inputStream.close()
                    outputStream.flush()
                    outputStream.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
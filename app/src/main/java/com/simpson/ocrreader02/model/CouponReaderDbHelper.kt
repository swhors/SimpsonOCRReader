package com.simpson.ocrreader02.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import com.simpson.ocrreader02.model.Coupon.CouponEntry

class CouponReaderDbHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    override fun onCreate(p0: SQLiteDatabase?) {
        Log.d("CouponReaderDbHelper.onCreate", "$SQL_CREATE_ENTRIES")
        p0!!.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(
        p0: SQLiteDatabase?,
        olderVersion: Int,
        newVersion: Int
    ) {
        p0!!.execSQL(SQL_DELETE_ENTRIES)
        onCreate(p0)
    }
    companion object {
        private const val DATABASE_NAME = "ocrreader.db"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${CouponEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${CouponEntry.COLUMN_NAME_TITLE} TEXT," +
                    "${CouponEntry.COLUMN_NAME_CREATED} NUMERIC," +
                    "${CouponEntry.COLUMN_NAME_DATESTART} TEXT," +
                    "${CouponEntry.COLUMN_NAME_DATEEND} TEXT," +
                    "${CouponEntry.COLUMN_NAME_PRICE} INTEGER," +
                    "${CouponEntry.COLUMN_NAME_USED} NUMERIC," +
                    "${CouponEntry.COLUMN_NAME_IMAGE} BLOB," +
                    "${CouponEntry.COLUMN_NAME_MESSAGE} TEXT)"
        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${CouponEntry.TABLE_NAME}"
    }
}

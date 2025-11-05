package com.simpson.ocrreader02.model

import android.graphics.Bitmap
import java.sql.Time

data class CouponData(val id: Int, val title: String, val created: Time, val dateStart: String, val dateEnd: String, val price: Int, val used: Boolean, val message: String, val image: Bitmap)

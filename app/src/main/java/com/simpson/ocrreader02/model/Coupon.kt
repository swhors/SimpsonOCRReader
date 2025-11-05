package com.simpson.ocrreader02.model

import android.provider.BaseColumns

object Coupon {
    object CouponEntry : BaseColumns {
        const val TABLE_NAME = "coupon"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_CREATED = "created"
        const val COLUMN_NAME_DATESTART = "date_start"
        const val COLUMN_NAME_DATEEND = "date_end"
        const val COLUMN_NAME_PRICE = "price"
        const val COLUMN_NAME_USED = "used"
        const val COLUMN_NAME_MESSAGE = "message"
        const val COLUMN_NAME_IMAGE = "image"
    }
}
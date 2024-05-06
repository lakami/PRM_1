package com.example.prm_zad1.model

import androidx.annotation.DrawableRes
import java.util.Calendar

data class Product(
    val id: Long,
    val name: String,
    val category: Int,
    val quantity: Int,
    val unit: String,
    val isDeleted: Boolean,
    val expirationDate: Long,
    @DrawableRes
    val resId: Int,
) {
    fun isExpired(): Boolean {
        val today = Calendar.getInstance()
        val expirationCalendar = Calendar.getInstance().apply {
            timeInMillis = expirationDate
        }

        if (today.get(Calendar.YEAR) == expirationCalendar.get(Calendar.YEAR)) {
            if (today.get(Calendar.MONTH) == expirationCalendar.get(Calendar.MONTH)) {
                return today.get(Calendar.DAY_OF_MONTH) > expirationCalendar.get(Calendar.DAY_OF_MONTH)
            } else {
                return today.get(Calendar.MONTH) > expirationCalendar.get(Calendar.MONTH)
            }
        } else {
            return today.get(Calendar.YEAR) > expirationCalendar.get(Calendar.YEAR)
        }
    }
}

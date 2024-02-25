package com.example.bitcoinwallet.Helpers

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    fun saveInputValue(inputValue: Double) {
        sharedPreferences.edit().putString("inputValue", inputValue.toString()).apply()
    }

    fun getInputValue(): Double {
        val defaultValue = 1.0
        return sharedPreferences.getString("inputValue", defaultValue.toString())?.toDouble()
            ?: defaultValue
    }
}
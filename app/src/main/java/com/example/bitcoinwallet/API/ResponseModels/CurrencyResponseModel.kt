package com.example.bitcoinwallet.API.ResponseModels

import com.google.gson.annotations.SerializedName

data class CurrencyResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("timestamp")
    val timestamp: Long,
    @SerializedName("base")
    val base: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("rates")
    val rates: Rates
)

data class Rates(
    @SerializedName("ZAR")
    val zar: Double,
    @SerializedName("USD")
    val usd: Double,
    @SerializedName("AUD")
    val aud: Double
)
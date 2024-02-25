package com.example.bitcoinwallet.API.ResponseModels

import com.google.gson.annotations.SerializedName

data class FluctuationResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("fluctuation")
    val fluctuation: Boolean,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("end_date")
    val endDate: String,
    @SerializedName("base")
    val base: String,
    @SerializedName("rates")
    val rates: FluctuationRates
)

data class FluctuationRates(
    @SerializedName("ZAR")
    val zar: Rate,
    @SerializedName("USD")
    val usd: Rate,
    @SerializedName("AUD")
    val aud: Rate
)

data class Rate(
    @SerializedName("start_rate")
    val startRate: Double,
    @SerializedName("end_rate")
    val endRate: Double,
    @SerializedName("change")
    val change: Double,
    @SerializedName("change_pct")
    val changePCT: Double
)

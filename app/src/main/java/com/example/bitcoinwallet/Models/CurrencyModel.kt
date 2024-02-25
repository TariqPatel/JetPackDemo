package com.example.bitcoinwallet.Models

import com.example.bitcoinwallet.FluctuationState

data class CurrencyModel(
    val currencyName: String,
    val currencyValue: String,
    val fluctuationValue: FluctuationState
)

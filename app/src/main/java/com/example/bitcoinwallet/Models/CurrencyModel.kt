package com.example.bitcoinwallet.Models

import com.example.bitcoinwallet.Enums.FluctuationState

data class CurrencyModel(
    val currencyName: String,
    val currencyValue: String,
    val fluctuationValue: FluctuationState
)

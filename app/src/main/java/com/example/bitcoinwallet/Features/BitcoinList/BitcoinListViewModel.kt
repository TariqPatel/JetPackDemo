package com.example.bitcoinwallet.Features.BitcoinList

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.example.bitcoinwallet.API.ResponseModels.CurrencyResponse
import com.example.bitcoinwallet.API.RetrofitClient
import com.example.bitcoinwallet.Helpers.Constants
import com.example.bitcoinwallet.Models.CurrencyModel
import com.example.bitcoinwallet.R

import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class MyViewModel : ViewModel() {
    //CurrencyList
    private val _currencyList = mutableStateOf <List<CurrencyModel>>(emptyList())
    val currencyList: State<List<CurrencyModel>> = _currencyList
    fun updateDataList(currencyList: List<CurrencyModel>) {
        _currencyList.value = currencyList
    }

    //Boolean for enabling and disabling button
    private val _isButtonEnabled = mutableStateOf(true)
    val isButtonEnabled: State<Boolean> get() = _isButtonEnabled
    fun setButtonEnabled(isEnabled: Boolean) {
        _isButtonEnabled.value = isEnabled
    }

    //Flag for showing toasts for some error handling
    private val _messageFlag = mutableStateOf(false)
    val messageFlag: State<Boolean> get() = _messageFlag
    fun toggleMessageFlag() {
        _messageFlag.value = !_messageFlag.value
    }

    //String for message shown to user in toast
    private val _toastMessage = mutableStateOf("")
    val toastMessage: State<String> get() = _toastMessage
    private fun showToastMessage(message: String) {
        _toastMessage.value = message
    }

    fun fetchCurrencyList(btcOwned: Double) {
        setButtonEnabled(false)
        val baseCurrency = Constants.BASE_CURRENCY
        val symbols = Constants.SYMBOLS
        val apiKey = Constants.API_KEY

        val call = RetrofitClient.currencyApiService.getCurrencyRates(baseCurrency, symbols, apiKey)

        call.enqueue(object : Callback<CurrencyResponse> {
            override fun onResponse(call: Call<CurrencyResponse>, response: Response<CurrencyResponse>) {
                setButtonEnabled(true)
                if (response.isSuccessful) {
                    val apiResponse = response.body()

                    // Perform currency calculations
                    val zarCalculated = apiResponse?.rates?.zar?.times(btcOwned) ?: 0.0
                    val usdCalculated = apiResponse?.rates?.usd?.times(btcOwned) ?: 0.0
                    val audCalculated = apiResponse?.rates?.aud?.times(btcOwned) ?: 0.0

                    // Create CurrencyModel objects
                    val zarCurrency = CurrencyModel("ZAR", zarCalculated.toString())
                    val usdCurrency = CurrencyModel("USD", usdCalculated.toString())
                    val audCurrency = CurrencyModel("AUD", audCalculated.toString())

                    // Create a list of CurrencyModel
                    val currencyList = listOf(zarCurrency, usdCurrency, audCurrency)

                    // Update ViewModel with the currency list
                    updateDataList(currencyList)
                } else {
                    toggleMessageFlag()
                    showToastMessage(message = "API call unsuccessful")
                }
            }

            override fun onFailure(call: Call<CurrencyResponse>, t: Throwable) {
                toggleMessageFlag()
                showToastMessage(message = "API call unsuccessful")
                setButtonEnabled(true)
            }
        })
    }
}
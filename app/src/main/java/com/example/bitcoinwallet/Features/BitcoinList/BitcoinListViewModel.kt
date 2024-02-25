package com.example.bitcoinwallet.Features.BitcoinList

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.bitcoinwallet.API.ResponseModels.CurrencyResponse
import com.example.bitcoinwallet.API.ResponseModels.FluctuationResponse
import com.example.bitcoinwallet.API.RetrofitClient
import com.example.bitcoinwallet.Enums.FluctuationState
import com.example.bitcoinwallet.Helpers.Constants
import com.example.bitcoinwallet.Models.CurrencyModel
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class BitcoinListViewModel : ViewModel() {

    fun fetchFluctuationCurrencyList(btcOwned: Double) {
        setButtonEnabled(false)
        val baseCurrency = Constants.BASE_CURRENCY
        val symbols = Constants.SYMBOLS
        val apiKey = Constants.API_KEY

        var currentDate = Date()
        val endDate = formatDateToString(currentDate)
        var startDate = formatDateToString(getDateBefore(currentDate, 1))

        val call = RetrofitClient.currencyApiService.getFluctuationCurrencyRates(
            startDate,endDate,baseCurrency, symbols, apiKey)

        call.enqueue(object : Callback<FluctuationResponse> {
            override fun onResponse(call: Call<FluctuationResponse>, response: Response<FluctuationResponse>) {
                setButtonEnabled(true)
                if (response.isSuccessful) {
                    val apiResponse = response.body()

                    var zar = apiResponse?.rates?.zar
                    var usd = apiResponse?.rates?.usd
                    var aud = apiResponse?.rates?.aud

                    // Perform currency calculations
                    val zarCalculated = zar?.endRate?.times(btcOwned) ?: 0.0
                    val usdCalculated = usd?.endRate?.times(btcOwned) ?: 0.0
                    val audCalculated = aud?.endRate?.times(btcOwned) ?: 0.0

                    var zarFluctuation = calculateRateDifference(
                        startRate = zar?.startRate, endRate = zar?.endRate)
                    var usdFluctuation = calculateRateDifference(
                        startRate = usd?.startRate, endRate = usd?.endRate)
                    var audFluctuation = calculateRateDifference(
                        startRate = aud?.startRate, endRate = aud?.endRate)

                    // Create CurrencyModel objects
                    val zarCurrency = CurrencyModel(
                        "ZAR", zarCalculated.toString(), zarFluctuation)
                    val usdCurrency = CurrencyModel(
                        "USD", usdCalculated.toString(), usdFluctuation)
                    val audCurrency = CurrencyModel(
                        "AUD", audCalculated.toString(), audFluctuation)

                    // Create a list of CurrencyModel
                    val currencyList = listOf(zarCurrency, usdCurrency, audCurrency)

                    // Update ViewModel with the currency list
                    updateDataList(currencyList)

                } else {
                    toggleMessageFlag()
                    showToastMessage(message = "API call unsuccessful")
                }
            }

            override fun onFailure(call: Call<FluctuationResponse>, t: Throwable) {
                toggleMessageFlag()
                showToastMessage(message = "API call unsuccessful")
                setButtonEnabled(true)
            }
        })
    }

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

    fun formatDateToString(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(date)
    }

    fun getDateBefore(inputDate: Date, daysBefore: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = inputDate
        calendar.add(Calendar.DAY_OF_YEAR, -daysBefore)
        return calendar.time
    }

    fun calculateRateDifference(startRate: Double?, endRate: Double?): FluctuationState {
        return when {
            endRate != null && startRate != null -> {
                when {
                    endRate == startRate -> FluctuationState.EQUAL
                    endRate > startRate -> FluctuationState.GAIN
                    endRate < startRate -> FluctuationState.LOSS
                    else -> FluctuationState.EQUAL
                }
            }
            else -> FluctuationState.EQUAL
        }
    }

    /*
       DEPRECATED but keeping it here for the assessment
       (Would usually remove it in a project as this function was
       replaced by the Fluctuation endpoint)
    */
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
                    val zarCurrency = CurrencyModel(
                        "ZAR", zarCalculated.toString(), FluctuationState.EQUAL)
                    val usdCurrency = CurrencyModel(
                        "USD", usdCalculated.toString(), FluctuationState.EQUAL)
                    val audCurrency = CurrencyModel(
                        "AUD", audCalculated.toString(), FluctuationState.EQUAL)

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
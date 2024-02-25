package com.example.bitcoinwallet

import com.example.bitcoinwallet.Enums.FluctuationState
import com.example.bitcoinwallet.Features.BitcoinList.BitcoinListViewModel
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class BitcoinListViewModelTest {

    @Test
    fun testFormatDateToString() {
        val viewModel = BitcoinListViewModel()
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2024-02-24")
        val formattedDate = viewModel.formatDateToString(date)
        assertEquals("2024-02-24", formattedDate)
    }

    @Test
    fun testGetDateBefore() {
        val viewModel = BitcoinListViewModel()
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2024-02-24")
        val expectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2024-02-23")
        val dateBefore = viewModel.getDateBefore(currentDate, 1)
        assertEquals(expectedDate, dateBefore)
    }

    @Test
    fun testCalculateRateDifferenceEqual() {
        val viewModel = BitcoinListViewModel()
        val fluctuationState = viewModel.calculateRateDifference(10.0, 10.0)
        assertEquals(FluctuationState.EQUAL, fluctuationState)
    }

    @Test
    fun testCalculateRateDifferenceGain() {
        val viewModel = BitcoinListViewModel()
        val fluctuationState = viewModel.calculateRateDifference(10.0, 15.0)
        assertEquals(FluctuationState.GAIN, fluctuationState)
    }

    @Test
    fun testCalculateRateDifferenceLoss() {
        val viewModel = BitcoinListViewModel()
        val fluctuationState = viewModel.calculateRateDifference(15.0, 10.0)
        assertEquals(FluctuationState.LOSS, fluctuationState)
    }

    @Test
    fun testCalculateRateDifferenceEqual_NullValues() {
        val viewModel = BitcoinListViewModel()
        val fluctuationState = viewModel.calculateRateDifference(null, null)
        assertEquals(FluctuationState.EQUAL, fluctuationState)
    }
}

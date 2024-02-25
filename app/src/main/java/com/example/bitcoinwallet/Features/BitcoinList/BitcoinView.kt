package com.example.bitcoinwallet.Features.BitcoinList

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bitcoinwallet.Enums.FluctuationState
import com.example.bitcoinwallet.Helpers.AppPreferences
import com.example.bitcoinwallet.R
import com.example.bitcoinwallet.ui.theme.BitcoinWalletTheme

@Composable
fun CurrencyConverterView(bitCoinListViewModel: BitcoinListViewModel = viewModel()) {
    val appPreferences = AppPreferences(LocalContext.current)
    var bitCoinAmount by remember {
        mutableStateOf(appPreferences.getInputValue())
    }
    val currencyList by bitCoinListViewModel.currencyList
    val context = LocalContext.current

    //Text from strings file used for View
    val fetchListBtnText: String = stringResource(id = R.string.fetch_bitcoin_btn)
    val viewBitcoinDescriptionText: String = stringResource(id = R.string.view_bitcoin_list_description)
    val bitcoinInputTitle: String = stringResource(id = R.string.bitcoin_input_text)
    val currencyText: String = stringResource(id = R.string.currency)
    val valueText: String = stringResource(id = R.string.value)
    val gainText: String = stringResource(id = R.string.gain)
    val equalText: String = stringResource(id = R.string.equal)
    val lossText: String = stringResource(id = R.string.loss)

    val messageFlag by bitCoinListViewModel.messageFlag
    val toastMessage by bitCoinListViewModel.toastMessage

    if (messageFlag) {
        LaunchedEffect(messageFlag) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
            bitCoinListViewModel.toggleMessageFlag()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = bitCoinAmount.toString(),
            onValueChange = {
                bitCoinAmount = it.toDoubleOrNull() ?: 0.0
            },
            label = { Text(bitcoinInputTitle) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    hideKeyboard(context)
                    appPreferences.saveInputValue(bitCoinAmount)
                }
            )
        )
        Button(
            onClick = {
                bitCoinListViewModel.fetchFluctuationCurrencyList(btcOwned = bitCoinAmount)
                appPreferences.saveInputValue(bitCoinAmount)
                hideKeyboard(context)
            },
            enabled = bitCoinListViewModel.isButtonEnabled.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(fetchListBtnText.uppercase())
        }
        if (bitCoinListViewModel.currencyList.value.isEmpty()) {
            Text(
                text = viewBitcoinDescriptionText,
                color = Color.Black,
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .clip(shape = MaterialTheme.shapes.medium) // Adjust the corner radius as needed
                    .background(Color.Black)
                    .border(2.dp, Color.Black)
            ) {
                itemsIndexed(currencyList) { index, currency ->
                    Text(
                        text = "$currencyText ${currency.currencyName}\n" +
                                "$valueText ${currency.currencyValue}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                    var fluctuationValue = ""
                    var color = Color.White

                    when (currency.fluctuationValue) {
                        FluctuationState.EQUAL -> {
                            fluctuationValue = equalText
                        }
                        FluctuationState.GAIN -> {
                            fluctuationValue = gainText
                            color = Color.Green
                        }
                        FluctuationState.LOSS -> {
                            fluctuationValue = lossText
                            color = Color.Red
                        }
                    }
                    Text(
                        text = fluctuationValue,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        color = color,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(Color.Black)
                    )
                }
            }
        }
    }
}

fun hideKeyboard(context: Context) {
    val view = (context as? ComponentActivity)?.findViewById<View>(android.R.id.content)
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(view?.windowToken, 0)
}

@Preview(showBackground = true)
@Composable
fun CurrencyConverterViewPreview() {
    BitcoinWalletTheme {
        CurrencyConverterView()
    }
}


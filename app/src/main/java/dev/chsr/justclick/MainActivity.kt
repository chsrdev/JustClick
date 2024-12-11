package dev.chsr.justclick

import android.icu.text.DecimalFormat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.chsr.justclick.ui.theme.JustClickTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

val df = DecimalFormat("#.##")

fun formatCoins(coins: Float): String {
    if (coins > 100000000) return "${df.format(coins / 1000000)}kk"
    if (coins > 100000) return "${df.format(coins / 1000f)}k"
    return df.format(coins)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val buttonColors = ButtonColors(
            containerColor = Color.Black,
            contentColor = Color.White,
            disabledContentColor = Color.LightGray,
            disabledContainerColor = Color.DarkGray
        )
        val scope = CoroutineScope(Dispatchers.Main)

        setContent {
            JustClickTheme {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val coins = remember { mutableFloatStateOf(99500f) }
                    val coinsForClick = remember { mutableFloatStateOf(1f) }
                    val autoCoins = remember { mutableFloatStateOf(0.1f) }
                    LaunchedEffect(scope) {
                        scope.launch {
                            while (isActive) {
                                coins.floatValue += autoCoins.floatValue
                                delay(1000)
                            }
                        }
                    }
                    Column(
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(
                            text = formatCoins(coins.floatValue),
                            fontSize = 48.sp,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(10.dp)
                        )
                        Button(
                            onClick = {
                                coins.floatValue += coinsForClick.floatValue
                            },
                            colors = buttonColors,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "+${formatCoins(coinsForClick.floatValue)}",
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Text(
                            text = "Auto: ${formatCoins(autoCoins.floatValue)}/s",
                            fontSize = 16.sp,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(10.dp)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 30.dp)
                    ) {
                        Button(
                            onClick = {
                                if (coins.floatValue >= coinsForClick.floatValue * 10f) {
                                    coins.floatValue -= coinsForClick.floatValue * 10f
                                    coinsForClick.floatValue += coinsForClick.floatValue / 10f
                                }
                            },
                            colors = buttonColors,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(bottom=10.dp)
                        ) {
                            Text(
                                text = formatCoins(coinsForClick.floatValue * 10f),
                                textAlign = TextAlign.Left,
                                modifier = Modifier.padding(end = 30.dp)
                            )
                            Text(
                                text = "Upgrade coins/click",
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "+${formatCoins(coinsForClick.floatValue / 10f)}",
                                textAlign = TextAlign.Right,
                                modifier = Modifier.padding(start = 30.dp)
                            )
                        }
                        Button(
                            onClick = {
                                if (coins.floatValue >= autoCoins.floatValue * 10f) {
                                    coins.floatValue -= autoCoins.floatValue * 10f
                                    autoCoins.floatValue += autoCoins.floatValue / 10f
                                }
                            },
                            colors = buttonColors,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = formatCoins(autoCoins.floatValue * 10f),
                                textAlign = TextAlign.Left,
                                modifier = Modifier.padding(end = 30.dp)
                            )
                            Text(
                                text = "Upgrade auto coins",
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "+${formatCoins(autoCoins.floatValue / 10f)}",
                                textAlign = TextAlign.Right,
                                modifier = Modifier.padding(start = 30.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


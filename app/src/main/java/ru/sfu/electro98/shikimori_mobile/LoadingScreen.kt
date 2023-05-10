package ru.sfu.electro98.shikimori_mobile

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sfu.electro98.shikimori_mobile.ui.theme.PrimaryColor
import kotlin.math.PI


@Composable
fun LoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val value by rememberInfiniteTransition().animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 3000,
                )
            )
        )
        CircularProgressIndicator(
            progress = 0.4f,
            color = PrimaryColor,
            strokeWidth = 4.dp,
            modifier = Modifier.rotate(value),
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    DefaultPreview {
        LoadingScreen()
    }
}

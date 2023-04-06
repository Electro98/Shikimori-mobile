package ru.sfu.electro98.shikimori_mobile

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.sfu.electro98.shikimori_mobile.ui.theme.HeaderLineColor
import ru.sfu.electro98.shikimori_mobile.ui.theme.HeaderSurfaceColor


fun Modifier.leftBorder(strokeWidth: Dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = - strokeWidthPx / 2
            val height = size.height

            drawLine(
                color = color,
                start = Offset(x = width, y = 0f),
                end = Offset(x = width , y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

@Composable
fun Header(
    title: String,
    paddingValues: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
) {
    Surface(
        modifier = Modifier
            .padding(start = 4.dp)
            .padding(paddingValues)
            .fillMaxWidth()
            .leftBorder(4.dp, color = HeaderLineColor),
        color = HeaderSurfaceColor,
    ) {
        Text(
            text = title,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommonComposablePreview() {
    DefaultPreview {
        Spacer(modifier = Modifier.height(4.dp))
        Header(title = "Sample text - Пример текста")
    }
}

package ru.sfu.electro98.shikimori_mobile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun FakeMapScreen() {
    Column {
        Header(title = "Nearest anime shops (will be)")
        Image(
            painter = painterResource(id = R.drawable.fake_map),
            contentDescription = "Very fake map of Krasnoyarsk",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 4.dp)
                .fillMaxWidth()
                .border(1.dp, color = MaterialTheme.colors.onSurface),
        )
    }
}
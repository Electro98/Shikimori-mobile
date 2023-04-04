package ru.sfu.electro98.shikimori_mobile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun AnimeList(list: List<AnimeShort>) {
    Column {
        Header(title = "Watched")
        Row(modifier = Modifier.padding(horizontal = 12.dp)) {
            Text(
                text = "#",
                modifier = Modifier
                    .fillMaxWidth(0.15f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .padding(start = 12.dp),
                fontSize = 18.sp,
            )
            Text(text = "Name", modifier = Modifier.fillMaxWidth(0.8f), fontSize = 18.sp)
        }
        Divider(modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 4.dp))
        LazyColumn(modifier = Modifier.padding(horizontal = 12.dp)) {
            itemsIndexed(list) { index, anime ->
                Row(modifier = Modifier.padding(vertical = 3.dp)) {
                    Text(text = "$index", modifier = Modifier
                        .fillMaxWidth(0.15f)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .padding(start = 12.dp))
                    Text(text = anime.name, modifier = Modifier.fillMaxWidth(0.98f))
                }
            }
        }
    }
}
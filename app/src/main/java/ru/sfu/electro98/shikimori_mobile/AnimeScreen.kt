package ru.sfu.electro98.shikimori_mobile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import ru.sfu.electro98.shikimori_mobile.entities.Anime


@Composable
fun FakeVerticalRating() {
    Image(
        painter = painterResource(id = R.drawable.star_empty),
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxWidth()
    )
    for (i in 0..3)
        Image(
            painter = painterResource(id = R.drawable.star_filled),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
        )
}


@Composable
fun AnimeScreen(anime: Anime) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Column {
            // Anime poster + rating
            Row {
                Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                    Text(
                        text = anime.name,
                        modifier = Modifier.padding(vertical = 6.dp),
                        fontSize = 24.sp,
                    )
                    AsyncImage(
                        model = urlToShiki(anime.image.original),
                        contentDescription = "Anime '${anime.name}' poster",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .width(250.dp)
                            .border(1.dp, color = MaterialTheme.colors.onSurface),
                    )
                }

                Column(modifier = Modifier.padding(top = 30.dp)) {
                    Text(
                        text = "Rating",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                    )
                    // Fake rating, easy
                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                    ) {
                        Column(
                            modifier = Modifier
                                .width(42.dp)
                                .padding(top = 4.dp)
                        ) {
                            FakeVerticalRating()
                            Text(text = "%.2f".format(anime.score), modifier = Modifier.padding(vertical = 4.dp))
                            Text(text = "MAL")
                        }
                        Spacer(modifier = Modifier.fillMaxWidth(0.2f))
                        Column(
                            modifier = Modifier
                                .width(42.dp)
                                .padding(top = 4.dp)
                        ) {
                            FakeVerticalRating()
                            Text(text = "%.2f".format(anime.score), modifier = Modifier.padding(vertical = 4.dp))
                            Text(text = "SHK")
                        }
                    }
                }
            }
            // Add to list
            Header(title = "Add to list")
            // Description
            Header(title = "Information")
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
            ) {
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(text = "Type: ")
                    Text(text = anime.kind)
                }
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(text = "Episodes: ")
                    Text(text = "${anime.episodes ?: '?'}/${anime.episodesTrulyAired}")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimeScreenPreview() {
    DefaultPreview {
        AnimeScreen(SampleData.new_anime())
    }
}

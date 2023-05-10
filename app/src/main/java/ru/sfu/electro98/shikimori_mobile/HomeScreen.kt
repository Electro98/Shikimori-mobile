package ru.sfu.electro98.shikimori_mobile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.smarttoolfactory.ratingbar.RatingBar
import ru.sfu.electro98.shikimori_mobile.entities.AnimePreview
import ru.sfu.electro98.shikimori_mobile.entities.AnimeStatus
import ru.sfu.electro98.shikimori_mobile.repository.AnimeRepository
import ru.sfu.electro98.shikimori_mobile.ui.theme.FormBordersColor
import ru.sfu.electro98.shikimori_mobile.ui.theme.FormSurfaceColor


@Composable
fun AnimePreview(navController: NavController, anime: AnimePreview) {
    Column(
        modifier = Modifier
            .padding(top = 2.dp, end = 16.dp)
            .width(120.dp)
            .clickable { navController.navigate("anime/${anime.id}") }
    ) {
        AsyncImage(
//            model = urlToShiki(anime.image.preview),
            model = ImageRequest.Builder(LocalContext.current)
                .data(urlToShiki(anime.image.preview))
                .crossfade(true)
                .build(),
            placeholder = painterResource(id = R.drawable.missing_preview),
            contentDescription = "Anime '${anime.name}' preview picture",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth(),
        )
        Text(text = anime.name, overflow = TextOverflow.Ellipsis, maxLines = 1)
    }
}

@Composable
fun ComingSoon(navController: NavController, animes: List<AnimePreview>) {
    Column {
        Header("Coming soon")
        Spacer(modifier = Modifier.height(2.dp))
        LazyRow(modifier = Modifier.padding(horizontal = 12.dp)) {
            items(animes) { anime ->
                AnimePreview(navController, anime)
            }
        }
    }
}


@Composable
fun LastAnime(anime: AnimeLong) {
    var rating by remember { mutableStateOf(3.5f) }
    Surface(
        modifier = Modifier
            .padding(12.dp)
            .border(1.dp, color = FormSurfaceColor),
        color = FormBordersColor,
    ) {
        Column(modifier = Modifier.padding(start = 6.dp, end = 6.dp, bottom = 6.dp)) {
            Text(
                text = "Last watched anime",
                modifier = Modifier.padding(vertical = 6.dp),
            )
            Row {
                Image(
                    painter = BitmapPainter(image = anime.image),
                    contentDescription = "Anime '${anime.name}' preview picture",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .width(120.dp)
                        .border(1.dp, color = FormBordersColor),
                )
                Column(modifier = Modifier.padding(horizontal = 4.dp)) {
                    Text(text = anime.name)
                    LinearProgressIndicator(
                        progress = anime.episodesWatched.toFloat() / anime.episodesAired,
                        modifier = Modifier
                            .height(12.dp)
                            .padding(top = 4.dp, bottom = 4.dp),
                        strokeCap = StrokeCap.Round,
                    )
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(text = anime.status)
                        Spacer(modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth())
                        Text(text = "Episodes ${anime.episodesWatched}/${anime.episodesAired}")
                    }
                    RatingBar(
                        rating = rating,
                        space = 4.dp,
                        imageVectorEmpty = ImageVector.vectorResource(id = R.drawable.star_empty),
                        imageVectorFFilled = ImageVector.vectorResource(id = R.drawable.star_filled),
                        tintEmpty = Color(0xFFD9D9D9),
                        tintFilled = Color(0xFF6274B1),
                        itemSize = 42.dp,
                        gestureEnabled = true,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        rating = it
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                value = "Text input.",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, color = FormBordersColor),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.background,
                ),
            )
        }
    }
}

@Composable
fun HomeScreen(navController: NavController, rep: AnimeRepository) {
    val onScreensAnime by rep.getAnimes(
        limit = 7,
        status = AnimeStatus.ongoing,
        order = "popularity",
    ).observeAsState()
    val ongoings = onScreensAnime?: listOf()
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Column {
            ComingSoon(animes = ongoings, navController = navController)
            LastAnime(anime = SampleData.last_anime())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DefaultPreview {
//        HomeScreen(navController = navController)
    }
}

package ru.sfu.electro98.shikimori_mobile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.sfu.electro98.shikimori_mobile.entities.Anime
import ru.sfu.electro98.shikimori_mobile.entities.RateStatus
import ru.sfu.electro98.shikimori_mobile.entities.UserRate
import ru.sfu.electro98.shikimori_mobile.repository.AnimeRepository
import ru.sfu.electro98.shikimori_mobile.repository.UserRatesRepository


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ControlStatusButton(anime: Anime, userRate: UserRate?, userRatesRepository: UserRatesRepository) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption: RateStatus? by remember { mutableStateOf(null) }
    userRate?.let { selectedOption = it.status }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {expanded = !expanded},
    ) {
        TextField(
            readOnly = true,
            value = selectedOption?.let { RateStatus.getName(it) } ?: "Add to list",
            onValueChange = { },
//            label = { Text("Label") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            RateStatus.values().forEach { rateStatus ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        selectedOption = rateStatus
                        if (userRate != null) {
                            userRate.status = rateStatus
                            userRatesRepository.updateUserRate(userRate)
                        } else {
                            userRatesRepository.addUserRate(anime.createUserRate(rateStatus))
                        }
                    }
                ){
                    Text(text = RateStatus.getName(rateStatus))
                }
            }
        }
    }
}


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
fun ShowAnime(anime: Anime, userRate: UserRate?, userRatesRepository: UserRatesRepository) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        color = MaterialTheme.colors.background,
    ) {
        Column {
            // Anime poster + rating
            Row {
                Column {
                    Text(
                        text = anime.name,
                        modifier = Modifier.padding(vertical = 6.dp),
                        fontSize = 24.sp,
                    )
                    AsyncImage(
//                        model = urlToShiki(anime.image.original),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(urlToShiki(anime.image.original))
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.missing_original),
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
            Header(title = "Add to list", paddingValues = PaddingValues(vertical = 4.dp))
            ControlStatusButton(anime, userRate, userRatesRepository = userRatesRepository)
            // Description
            Header(title = "Information", paddingValues = PaddingValues(vertical = 4.dp))
            Column {
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


@Composable
fun AnimeScreen(animeId: Int, animeRepository: AnimeRepository, userRatesRepository: UserRatesRepository) {
    val foundAnime by animeRepository.getById(animeId).observeAsState()
//    val foundAnime by animeRepository.getByIdFlow(animeId).collectAsState(initial = null)
    val userRate by userRatesRepository.getByAnimeId(animeId).collectAsState(initial = null)
    SideEffect {
        Log.d("Composition", "AnimeScreen was composed, UserRate: $userRate, FoundAnime: $foundAnime")
    }
    foundAnime.let {
        if (it != null) {
            ShowAnime(it, userRate, userRatesRepository)
        } else {
            LoadingScreen()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AnimeScreenPreview() {
    DefaultPreview {
//        ShowAnime(SampleData.new_anime())
    }
}

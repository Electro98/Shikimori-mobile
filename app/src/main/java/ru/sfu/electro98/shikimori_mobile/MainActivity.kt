package ru.sfu.electro98.shikimori_mobile

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.smarttoolfactory.ratingbar.RatingBar
import ru.sfu.electro98.shikimori_mobile.ui.theme.ShikimoriMobileTheme
import java.time.ZoneId
import java.time.ZonedDateTime


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var navController = rememberNavController()
            Scaffold(
                topBar = { TopSearchBar() },
                bottomBar = { BottomBar(navController = navController) },
            ) { contentPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.padding(contentPadding)
                ) {
                    composable("home") { StartScreen(navController) }
                    composable("anime") { AnimeScreen(anime = SampleData.one_piece()) }
                    composable("map") { FakeMapScreen() }
                    composable("list") { AnimeList(SampleData.animes()) }
                    composable("settings") { NotImplementedScreen() }
                    composable("profile") { NotImplementedScreen() }
                }
            }
        }
    }
}

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
fun Header(title: String) {
    Surface(
        modifier = Modifier
            .padding(start = 16.dp, end = 12.dp, top = 4.dp, bottom = 4.dp)
            .fillMaxWidth()
            .leftBorder(4.dp, MaterialTheme.colors.primaryVariant),
        color = MaterialTheme.colors.primarySurface
    ) {
        Text(
            text = title,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}


data class AnimeShort(val name: String, val image: ImageBitmap) {
    companion object {
        @Composable
        fun fromResource(name: String, id: Int): AnimeShort {
            return AnimeShort(name, ImageBitmap.imageResource(id))
        }
    }
}

data class AnimeLong(
    val name: String,
    val image: ImageBitmap,
    val kind: String,
    val score: Float,
    val status: String,
    val episodes: Int?,
    val episodesAired: Int,
    val duration: String,
    val airedOn: ZonedDateTime?,
    val rating: String,
    val genres: List<String>,
    val userScore: Int?,
    val userStatus: String?,
    val episodesWatched: Int,
) {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        @Composable
        fun fromResource(
            name: String,
            id: Int,
            kind: String = "TV series",
            score: Float = 0f,
            status: String = "ongoing",
            episodes: Int? = null,
            episodesAired: Int = 0,
            duration: String = "24 min.",
            airedOn: ZonedDateTime? = ZonedDateTime.of(1999, 10, 20, 0, 0, 0, 0, ZoneId.systemDefault()),
            rating: String = "PG-13",
            genres: List<String> = listOf("Action", "Adventure", "Fantasy", "Shonen"),
            userScore: Int? = null,
            userStatus: String? = null,
            episodesWatched: Int = 0,
        ): AnimeLong {
            return AnimeLong(name, ImageBitmap.imageResource(id), kind, score, status, episodes, episodesAired, duration, airedOn, rating, genres, userScore, userStatus, episodesWatched)
        }
    }
}


@Composable
fun AnimePreview(navController: NavController, anime: AnimeShort) {
    Column(
        modifier = Modifier
            .padding(top = 2.dp, end = 16.dp)
            .width(120.dp)
            .clickable { navController.navigate("anime") }
    ) {
        Image(
            painter = BitmapPainter(image = anime.image),
            contentDescription = "Anime '${anime.name}' preview picture",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth(),
        )
        Text(text = anime.name, overflow = TextOverflow.Ellipsis, maxLines = 1)
    }
}

@Composable
fun ComingSoon(navController: NavController, animes: List<AnimeShort>) {
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
            .border(1.dp, color = MaterialTheme.colors.onSurface),
        color = MaterialTheme.colors.surface,
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
                        .border(1.dp, color = MaterialTheme.colors.onSurface),
                )
                Column(modifier = Modifier.padding(horizontal = 4.dp)) {
                    Text(text = anime.name)
                    LinearProgressIndicator(
                        progress = anime.episodesWatched.toFloat() / anime.episodesAired,
                        modifier = Modifier
                            .height(12.dp)
                            .padding(top = 4.dp, bottom = 4.dp),
//                        strokeCap = StrokeCap.Round,  // Will be available in 1.4.0-alpha04
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
                    .border(1.dp, color = MaterialTheme.colors.onSurface),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.background,
                ),
            )
        }
    }
}


object SampleData {
    @Composable
    fun animes(): List<AnimeShort> {
        return listOf(
            AnimeShort.fromResource("One Piece", R.drawable.one_piece),
            AnimeShort.fromResource("My hero academy", R.drawable.my_hero_academy),
            AnimeShort.fromResource("The Angel Next Door Spoils Me Rotten", R.drawable.angel_next_door),
            AnimeShort.fromResource("Tomo-chan Is a Girl!", R.drawable.tomo_chan_is_a_girl),
        )
    }
    @SuppressLint("NewApi")
    @Composable
    fun last_anime(): AnimeLong {
        return AnimeLong.fromResource("Angel beats!", R.drawable.angel_beats, status = "released", episodes = 13, episodesAired = 13, userScore = 10, userStatus = "watched", episodesWatched = 13)
    }

    @SuppressLint("NewApi")
    @Composable
    fun one_piece(): AnimeLong {
        return AnimeLong.fromResource("One Piece", R.drawable.one_piece, score = 8.66f)
    }
}


@Composable
fun StartScreen(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Column {
            ComingSoon(animes = SampleData.animes(), navController = navController)
            LastAnime(anime = SampleData.last_anime())
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
            painter = painterResource(id = R.drawable.search),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
        )
}


@Composable
fun AnimeScreen(anime: AnimeLong) {
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
                    Image(
                        painter = BitmapPainter(anime.image),
                        contentDescription = "Anime '${anime.name}' poster",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .width(250.dp)
                            .border(1.dp, color = MaterialTheme.colors.onSurface)
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
                    Text(text = "${anime.episodes ?: '?'}/${anime.episodesAired}")
                }
            }
        }
    }
}


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


@Composable
fun TopSearchBar() {
    TopAppBar(
        contentColor = Color.White,
        title = {
            Text(text = "SHIKIMORI ", fontSize = 24.sp)
            Text(text = "mobile", fontSize = 12.sp)
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.star_filled),
                    contentDescription = "Search icon",
                    modifier = Modifier.height(32.dp)
                )
            }
        },
        modifier = Modifier.height(45.dp)
    )
}


@Composable
fun NotImplementedScreen() {
    Column {
        Header(title = "This page is still under construction")
        Image(
            painter = painterResource(id = R.drawable.not_found),
            contentDescription = "This page not found",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 4.dp)
                .fillMaxWidth(),
        )
    }
}


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


sealed class BottomBarItems(val route: String, val iconId: Int) {
    object Home :BottomBarItems("home", R.drawable.home)
    object Map :BottomBarItems("map", R.drawable.map)
    object List :BottomBarItems("list", R.drawable.plus)
    object Settings :BottomBarItems("settings", R.drawable.settings)
    object Profile :BottomBarItems("profile", R.drawable.unknown_user)
}

@Composable
fun BottomBar(navController: NavHostController) {
    BottomNavigation {
        val currentRoute = navController.currentDestination?.route
        val items = listOf(
            BottomBarItems.Home,
            BottomBarItems.Map,
            BottomBarItems.List,
            BottomBarItems.Settings,
            BottomBarItems.Profile,
        )
        items.forEach { item ->
            BottomNavigationItem(
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconId),
                        contentDescription = null,
                        modifier = Modifier.height(24.dp)
                    )
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ShikimoriMobileTheme {
        ShikimoriMobileTheme {
            // A surface container using the 'background' color from the theme
            val navController = rememberNavController()
            Scaffold(
                topBar = { TopSearchBar() },
                bottomBar = { BottomBar(navController = navController) },
            ) { contentPadding ->
                Column(modifier = Modifier.padding(contentPadding)) {
//                    AnimeScreen(SampleData.one_piece())
//                    FakeMapScreen()
//                    NotImplementedScreen()
                    AnimeList(SampleData.animes())
                }
            }
        }
    }
}
package ru.sfu.electro98.shikimori_mobile

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                    composable("home") { HomeScreen(navController) }
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
                    painter = painterResource(id = R.drawable.search),
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
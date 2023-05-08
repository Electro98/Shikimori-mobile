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
import com.google.gson.Gson
import ru.sfu.electro98.shikimori_mobile.entities.Anime
import ru.sfu.electro98.shikimori_mobile.ui.theme.ShikimoriMobileTheme
import java.time.ZoneId
import java.time.ZonedDateTime


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val anime = SampleData.new_anime()
        println(anime)
        println(urlToShiki(anime.image.original))
        setContent {
            ShikimoriMobileTheme {
                val navController = rememberNavController()
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
                        composable("anime") { AnimeScreen(anime = SampleData.new_anime()) }
                        composable("map") { FakeMapScreen() }
                        composable("list") { AnimeList(SampleData.animes()) }
                        composable("settings") { NotImplementedScreen() }
                        composable("profile") { NotImplementedScreen() }
                    }
                }
            }
        }
    }
}


fun urlToShiki(url: String): String {
    return "https://shikimori.me$url"
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

    fun new_anime(): Anime {
        return Gson().fromJson("{\"id\":1,\"name\":\"Cowboy Bebop\",\"russian\":\"Ковбой Бибоп\",\"image\":{\"original\":\"/system/animes/original/1.jpg?1674378220\",\"preview\":\"/system/animes/preview/1.jpg?1674378220\",\"x96\":\"/system/animes/x96/1.jpg?1674378220\",\"x48\":\"/system/animes/x48/1.jpg?1674378220\"},\"url\":\"/animes/1-cowboy-bebop\",\"kind\":\"tv\",\"score\":\"8.75\",\"status\":\"released\",\"episodes\":26,\"episodes_aired\":0,\"aired_on\":\"1998-04-03\",\"released_on\":\"1999-04-24\",\"rating\":\"r_plus\",\"english\":[\"Cowboy Bebop\"],\"japanese\":[\"カウボーイビバップ\"],\"synonyms\":[],\"license_name_ru\":\"Ковбой Бибоп\",\"duration\":24,\"description\":\"2071 год. Человечество колонизировало всю Солнечную Систему, основав колонии от Венеры до Юпитера. Но десятилетия тому назад из-за техногенной катастрофы была уничтожена Луна. Последствия оказались катастрофическими: непрерывные метеоритные дожди сделали жизнь на поверхности Земли невозможной, а в первые недели после катастрофы погибло 4,7 миллиарда человек. Большая часть выживших перебралась в колонии на другие планеты.\\r\\nСо временем по всей Солнечной Системе разрослись и набрали силу преступные синдикаты, для борьбы с которыми правительство возродило древнюю практику охоты за головами. Отныне охотники за головами разъезжают по всей Солнечной Системе в поисках целей.\\r\\nСпайк Шпигель [スパイク・スピーゲル] и Джет Блэк [ジェット・ブラック] — охотники. Волею судьбы они оказались на космическом корабле «Bebop 268710». Путешествуя вместе, они подбирают Фэй Валентайн [フェイ・バレンタイン] — очаровательную картёжницу с невероятно огромным долгом, Радикал Эдварда [エドワード・ウォン・ハウ・ペペル・チブルスキー4世] — компьютерного гения и генетически модифицированную собаку Эйн [アイン].\\r\\nНа борту «Bebop» судьба сводит четырёх человек и одну собаку, и так начинаются их совместные приключения...\",\"description_html\":\"<div class=\\\"b-text_with_paragraphs\\\">2071 год. Человечество колонизировало всю Солнечную Систему, основав колонии от Венеры до Юпитера. Но десятилетия тому назад из-за техногенной катастрофы была уничтожена Луна. Последствия оказались катастрофическими: непрерывные метеоритные дожди сделали жизнь на поверхности Земли невозможной, а в первые недели после катастрофы погибло 4,7 миллиарда человек. Большая часть выживших перебралась в колонии на другие планеты.<br>Со временем по всей Солнечной Системе разрослись и набрали силу преступные синдикаты, для борьбы с которыми правительство возродило древнюю практику охоты за головами. Отныне охотники за головами разъезжают по всей Солнечной Системе в поисках целей.<br><a href=\\\"https://shikimori.one/characters/1-spike-spiegel\\\" title=\\\"Spike Spiegel\\\" class=\\\"bubbled b-link\\\" data-tooltip_url=\\\"https://shikimori.one/characters/1-spike-spiegel/tooltip\\\" data-attrs=\\\"{&quot;id&quot;:1,&quot;type&quot;:&quot;character&quot;,&quot;name&quot;:&quot;Spike Spiegel&quot;,&quot;russian&quot;:&quot;Спайк Шпигель&quot;}\\\">Спайк Шпигель</a> и <a href=\\\"https://shikimori.one/characters/3-jet-black\\\" title=\\\"Jet Black\\\" class=\\\"bubbled b-link\\\" data-tooltip_url=\\\"https://shikimori.one/characters/3-jet-black/tooltip\\\" data-attrs=\\\"{&quot;id&quot;:3,&quot;type&quot;:&quot;character&quot;,&quot;name&quot;:&quot;Jet Black&quot;,&quot;russian&quot;:&quot;Джет Блэк&quot;}\\\">Джет Блэк</a> — охотники. Волею судьбы они оказались на космическом корабле «Bebop 268710». Путешествуя вместе, они подбирают <a href=\\\"https://shikimori.one/characters/2-faye-valentine\\\" title=\\\"Faye Valentine\\\" class=\\\"bubbled b-link\\\" data-tooltip_url=\\\"https://shikimori.one/characters/2-faye-valentine/tooltip\\\" data-attrs=\\\"{&quot;id&quot;:2,&quot;type&quot;:&quot;character&quot;,&quot;name&quot;:&quot;Faye Valentine&quot;,&quot;russian&quot;:&quot;Фэй Валентайн&quot;}\\\">Фэй Валентайн</a> — очаровательную картёжницу с невероятно огромным долгом, <a href=\\\"https://shikimori.one/characters/16-edward-wong-hau-pepelu-tivrusky-iv\\\" title=\\\"Edward Wong Hau Pepelu Tivrusky IV\\\" class=\\\"bubbled b-link\\\" data-tooltip_url=\\\"https://shikimori.one/characters/16-edward-wong-hau-pepelu-tivrusky-iv/tooltip\\\" data-attrs=\\\"{&quot;id&quot;:16,&quot;type&quot;:&quot;character&quot;,&quot;name&quot;:&quot;Edward Wong Hau Pepelu Tivrusky IV&quot;,&quot;russian&quot;:&quot;Радикал Эдвард&quot;}\\\">Радикал Эдварда</a> — компьютерного гения и генетически модифицированную собаку <a href=\\\"https://shikimori.one/characters/4-ein\\\" title=\\\"Ein\\\" class=\\\"bubbled b-link\\\" data-tooltip_url=\\\"https://shikimori.one/characters/4-ein/tooltip\\\" data-attrs=\\\"{&quot;id&quot;:4,&quot;type&quot;:&quot;character&quot;,&quot;name&quot;:&quot;Ein&quot;,&quot;russian&quot;:&quot;Эйн&quot;}\\\">Эйн</a>.<br>На борту «Bebop» судьба сводит четырёх человек и одну собаку, и так начинаются их совместные приключения...</div>\",\"description_source\":null,\"franchise\":\"cowboy_bebop\",\"favoured\":false,\"anons\":false,\"ongoing\":false,\"thread_id\":3503,\"topic_id\":3503,\"myanimelist_id\":1,\"rates_scores_stats\":[{\"name\":10,\"value\":21307},{\"name\":9,\"value\":12483},{\"name\":8,\"value\":8880},{\"name\":7,\"value\":4063},{\"name\":6,\"value\":1413},{\"name\":5,\"value\":611},{\"name\":4,\"value\":231},{\"name\":3,\"value\":75},{\"name\":2,\"value\":56},{\"name\":1,\"value\":125}],\"rates_statuses_stats\":[{\"name\":\"Запланировано\",\"value\":40625},{\"name\":\"Просмотрено\",\"value\":73352},{\"name\":\"Смотрю\",\"value\":11407},{\"name\":\"Брошено\",\"value\":4056},{\"name\":\"Отложено\",\"value\":5245}],\"updated_at\":\"2023-03-30T15:13:32.528+03:00\",\"next_episode_at\":null,\"fansubbers\":[\"Dragon'Drop\",\"Max Skuratov\",\"Сергей Светличный\",\"Suzaku\",\"Faddeich\",\"Netflix\"],\"fandubbers\":[\"Amazing Dubbing\",\"Е. Лурье\",\"SHIZA Project\",\"SkyFy\",\"Digital Force\",\"3df voice\",\"Ю. Сербин\",\"NaimanFilm\",\"AniLibria\",\"KANSAI Studio\"],\"licensors\":[\"Netflix\"],\"genres\":[{\"id\":1,\"name\":\"Action\",\"russian\":\"Экшен\",\"kind\":\"anime\"},{\"id\":24,\"name\":\"Sci-Fi\",\"russian\":\"Фантастика\",\"kind\":\"anime\"},{\"id\":29,\"name\":\"Space\",\"russian\":\"Космос\",\"kind\":\"anime\"}],\"studios\":[{\"id\":14,\"name\":\"Sunrise\",\"filtered_name\":\"Sunrise\",\"real\":true,\"image\":\"/system/studios/original/14.jpg?1630137154\"}],\"videos\":[{\"id\":28853,\"url\":\"https://youtu.be/TdBfgwHmQsQ\",\"image_url\":\"http://img.youtube.com/vi/TdBfgwHmQsQ/hqdefault.jpg\",\"player_url\":\"http://youtube.com/embed/TdBfgwHmQsQ\",\"name\":\"Трейлер (Funimation)\",\"kind\":\"pv\",\"hosting\":\"youtube\"},{\"id\":8195,\"url\":\"https://youtu.be/kGzneAKtAgg\",\"image_url\":\"http://img.youtube.com/vi/kGzneAKtAgg/hqdefault.jpg\",\"player_url\":\"http://youtube.com/embed/kGzneAKtAgg\",\"name\":\"Blu-ray BOX\",\"kind\":\"cm\",\"hosting\":\"youtube\"}],\"screenshots\":[{\"original\":\"/system/screenshots/original/b1276021bfb0fc514c35bb106361dd2bcb20f967.jpg?1521803518\",\"preview\":\"/system/screenshots/x332/b1276021bfb0fc514c35bb106361dd2bcb20f967.jpg?1521803518\"},{\"original\":\"/system/screenshots/original/210f496943c6aa7c0e8d875f3d1dae235ac009b2.jpg?1521803527\",\"preview\":\"/system/screenshots/x332/210f496943c6aa7c0e8d875f3d1dae235ac009b2.jpg?1521803527\"}],\"user_rate\":{\"id\":109728860,\"score\":0,\"status\":\"planned\",\"text\":null,\"episodes\":0,\"chapters\":0,\"volumes\":0,\"text_html\":\"\",\"rewatches\":0,\"created_at\":\"2021-10-13T20:05:43.211+03:00\",\"updated_at\":\"2021-10-13T20:05:43.211+03:00\"}}", Anime::class.java);
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
//            painter = ,
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
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            )
        }
    }
}


data class DefaultPreviewContext(val navController: NavHostController)

@Composable
fun DefaultPreview(content: @Composable() DefaultPreviewContext.() -> Unit) {
    ShikimoriMobileTheme {
        // A surface container using the 'background' color from the theme
        val navController = rememberNavController()
        Scaffold(
            topBar = { TopSearchBar() },
            bottomBar = { BottomBar(navController = navController) },
        ) { contentPadding ->
            Column(modifier = Modifier.padding(contentPadding)) {
                content(DefaultPreviewContext(navController))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BarsPreview() {
    DefaultPreview {
        HomeScreen(navController = navController)
    }
}

package ru.sfu.electro98.shikimori_mobile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.sfu.electro98.shikimori_mobile.entities.RateStatus
import ru.sfu.electro98.shikimori_mobile.entities.UserRate
import ru.sfu.electro98.shikimori_mobile.repository.AnimeRepository
import ru.sfu.electro98.shikimori_mobile.repository.UserRatesRepository


fun ratesSection(status: RateStatus, userRates: List<UserRate>, navController: NavController, scope: LazyListScope) {
    scope.item {
        Header(
            title = RateStatus.getName(status),
            paddingValues = PaddingValues(horizontal = 0.dp, vertical = 4.dp),
        )
    }
    scope.item {
        Row {
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
    }
    scope.item { Divider(modifier = Modifier.padding(bottom = 4.dp)) }

    scope.itemsIndexed(userRates) { index, userRate ->
        Row(modifier = Modifier.padding(vertical = 3.dp)) {
            Text(
                text = "$index",
                modifier = Modifier
                    .fillMaxWidth(0.15f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .padding(start = 12.dp),
            )
            Text(
                text = userRate.name,
                modifier = Modifier
                    .fillMaxWidth(0.98f)
                    .clickable {
                        navController.navigate("anime/${userRate.targetId}")
                    },
            )
        }
    }
}


@Composable
fun AnimeList(navController: NavController, groupedRates: Map<RateStatus, List<UserRate>>) {
    LazyColumn(modifier = Modifier.padding(horizontal = 12.dp)) {
        for (rateStatus in RateStatus.values()) {
            groupedRates[rateStatus]?.let { rates ->
                ratesSection(status = rateStatus, userRates = rates, navController = navController, scope = this)
            }
        }
    }
}


@Composable
fun AnimeListScreen(navController: NavController, userRatesRepository: UserRatesRepository) {
//    val groupedRates by userRatesRepository.getGroupedByStatus().observeAsState()
//    val groupedRates by userRatesRepository.getGroupedByStatusFlow().collectAsState(initial = null)
    val rates by userRatesRepository.getAllRates().collectAsState(initial = null)
    val groupedRates = rates?.groupBy { it.status }
    SideEffect {
        println("Rates: $groupedRates")
    }
    groupedRates.let {
        if (it != null) {
            AnimeList(navController = navController, groupedRates = it)
        } else {
            LoadingScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimeListScreenPreview() {
    DefaultPreview {
//        AnimeList(SampleData.animes())
    }
}

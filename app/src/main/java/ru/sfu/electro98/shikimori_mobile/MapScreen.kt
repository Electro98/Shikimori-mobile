package ru.sfu.electro98.shikimori_mobile

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.getDrawable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.TextStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


val PointsOfInterest = mapOf(
    Pair(Point(56.008408, 92.885077), "Совятня"),
    Pair(Point(56.010204, 92.863107), "Loot4Geek"),
    Pair(Point(56.012274, 92.859269), "Panda-Shop"),
    Pair(Point(56.018995, 93.005258), "Умные игры"),
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DumbMapScreen() {
    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle()
    Column {
        Header(title = "Krasnoyarsk anime stores")
        AndroidView(
            factory = { mapView },
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 4.dp)
                .fillMaxWidth()
                .border(1.dp, color = MaterialTheme.colors.onSurface),
        ) {
            CoroutineScope(Dispatchers.Main).launch {
                mapView.map.move(
                    CameraPosition(Point(56.0184, 92.8672), 11.0f, 0f, 0f),
                    Animation(Animation.Type.SMOOTH, 0f),
                    null
                )
                val textStyle = TextStyle(8f, null, null, TextStyle.Placement.BOTTOM, 0f, true, true)
                val icon = ImageProvider.fromResource(context, R.drawable.map_interest_point)
                for ((point, name) in PointsOfInterest) {
                    val placemark = mapView.map.mapObjects.addPlacemark(point, icon)
                    placemark.setText(name)
                    placemark.setTextStyle(textStyle)
                }
            }
        }
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.mapview
        }
    }

    // Makes MapView follow the lifecycle of this composable
    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {}
                Lifecycle.Event.ON_START -> {
                    MapKitFactory.getInstance().onStart()
                    mapView.onStart()
                }
                Lifecycle.Event.ON_RESUME -> {}
                Lifecycle.Event.ON_PAUSE -> {}
                Lifecycle.Event.ON_STOP -> {
                    mapView.onStop()
                    MapKitFactory.getInstance().onStop()
                }
                Lifecycle.Event.ON_DESTROY -> {}
                else -> throw IllegalStateException()
            }
        }
    }

package dev.snvdr.heartrate.features.homepage.hp_2

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.snvdr.common.Dimens
import dev.snvdr.common.DrawBackgroundEllipse
import dev.snvdr.common.LinearProgressIndicatorWithPercentage
import dev.snvdr.heart.uikit.R
import dev.snvdr.heart.uikit.ui.theme.MainBlue
import dev.snvdr.heart.uikit.ui.theme.SecondaryBlue
import dev.snvdr.heartrate.features.homepage.hp_2.camera.CameraFeature
import dev.snvdr.heart.uikit.R as UiKit

@Composable
fun HomePage2FeatureUI() {
    val viewModel: HomePage2ViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomePage2UIFeatureContent(
        state,
        isCameraCovered = viewModel::isCameraCovered,
        onCloseClick = { viewModel.onNavigateBack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage2UIFeatureContent(
    state: HomePageUIState,
    isCameraCovered: (Boolean) -> Unit,
    onCloseClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MainBlue),
                title = {},
                actions = {
                    IconButton(onClick = { onCloseClick() }) {
                        Icon(
                            modifier = Modifier.size(Dimens.dp_28),
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPaddings ->
        DrawBackgroundEllipse()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            CameraSection(
                modifier = Modifier.weight(1f),
                isCameraCovered = isCameraCovered
            )
            MeasurementSection(
                modifier = Modifier.weight(5f),
                isMeasurement = state.isCameraCovered,
                bpm = state.bpm,
                progress = state.progressOfMeasurement
            )
        }
    }
}

@Composable
private fun CameraSection(
    modifier: Modifier = Modifier,
    isCameraCovered: (Boolean) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(Dimens.dp_4)
                .fillMaxWidth(0.12f)
                .aspectRatio(1f)
                .background(SecondaryBlue, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(0.9f)
                    .aspectRatio(1f)
                    .background(Color.Transparent, shape = CircleShape)
            ) {
                CameraFeature(
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(1f)
                        .clip(CircleShape),
                    isCameraCovered = isCameraCovered
                )
            }
        }
        Text(
            text = stringResource(UiKit.string.no_finger),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = stringResource(UiKit.string.place_finger_on_cam),
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }
}

@Composable
private fun MeasurementSection(
    modifier: Modifier = Modifier,
    isMeasurement: Boolean,
    bpm: Int,
    progress: Float,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        HeartWithText(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .fillMaxHeight(0.5f),
            bpm = bpm
        )
        AnimatedContent(targetState = isMeasurement, label = "") { isMeasure ->
            if (isMeasure) {
                LinearProgressIndicatorWithPercentage(
                    progress = progress,
                    onLoadingEnd = {}
                )
            } else {
                Image(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .fillMaxHeight(0.45f),
                    painter = painterResource(UiKit.drawable.hand_phone),
                    contentDescription = null
                )
            }
        }
    }
}
@Composable
fun HeartWithText(
    modifier: Modifier = Modifier,
    bpm: Int,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    var isHeartBeating by remember { mutableStateOf(false) }

    DisposableEffect(bpm) {
        isHeartBeating = bpm != 0
        onDispose { }
    }

    val heartScale by if (isHeartBeating) {
        val infiniteTransition = rememberInfiniteTransition(label = "")
        infiniteTransition.animateFloat(
            initialValue = 1.1f,
            targetValue = 0.9f,
            animationSpec = infiniteRepeatable(
                animation = tween(500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )
    } else {
        remember { mutableFloatStateOf(1f) }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Image(
            modifier = Modifier
                .fillMaxSize()
                .scale(heartScale),  // Apply the animated scale value
            painter = painterResource(R.drawable.empty_heart),
            contentDescription = null,
        )
        Column(
            modifier = Modifier.fillMaxHeight(0.65f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (bpm != 0) bpm.toString() else "_ _",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 75.sp,
                color = Color.White
            )
            Text(text = "bpm", style = MaterialTheme.typography.titleMedium, color = Color.White)
            Spacer(
                modifier = Modifier
                    .height(screenHeight * 0.05f)
                    .width(screenHeight * 0.05f)
            )
        }
    }

    // Observe changes in the bpm value

}
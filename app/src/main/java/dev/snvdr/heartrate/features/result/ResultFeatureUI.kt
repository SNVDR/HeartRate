package dev.snvdr.heartrate.features.result

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.snvdr.common.Dimens
import dev.snvdr.common.DrawBackgroundEllipse
import dev.snvdr.common.TopBarSection
import dev.snvdr.common.toDate
import dev.snvdr.common.toTime
import dev.snvdr.heart.uikit.ui.theme.CardGray
import dev.snvdr.heart.uikit.ui.theme.CardLightGray
import dev.snvdr.heart.uikit.ui.theme.HeartRateTheme
import dev.snvdr.heart.uikit.ui.theme.MainBlue
import dev.snvdr.heart.uikit.ui.theme.MainGreen
import dev.snvdr.heart.uikit.ui.theme.MainRed
import dev.snvdr.heart.uikit.ui.theme.MainTurquoise
import dev.snvdr.heart.uikit.ui.theme.ResultCardColor
import dev.snvdr.heart.uikit.ui.theme.SecondaryGreen
import dev.snvdr.heart.uikit.ui.theme.ThirdBlue
import dev.snvdr.heart.uikit.ui.theme.ThirdRed
import dev.snvdr.heartrate.models.HeartRate
import kotlinx.coroutines.delay
import dev.snvdr.heart.uikit.R as UiKit

@Composable
fun ResultFeatureUI(id: Long) {
    val viewModel: ResultFeatureViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        viewModel.getById(id)
    }

    state.heartRate?.let { heartRate ->
        ResultUIContent(
            heartRate = heartRate,
            onHistoryClick = { viewModel.onNavigateToHistory() },
            onDoneClick = { viewModel.onNavigateBack() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultUIContent(heartRate: HeartRate, onHistoryClick: () -> Unit, onDoneClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MainRed),
                title = {
                    Text(
                        text = stringResource(UiKit.string.result),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Normal
                    )
                },
                actions = {
                    TopBarSection(onHistoryClick = onHistoryClick)
                }
            )
        }
    ) { innerPaddings ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings)
        ) {
            DrawBackgroundEllipse()
            ResultCard(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.35f),
                heartRate = heartRate
            )
            Button(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.9f),
                onClick = onDoneClick,
                colors = ButtonDefaults.buttonColors(containerColor = MainRed)
            ) {
                Text(text = stringResource(UiKit.string.continuee))
            }
        }
    }
}

@Composable
fun ResultCard(modifier: Modifier = Modifier, heartRate: HeartRate) {
    var isBPMSlow by remember { mutableStateOf(false) }
    var isBPMNormal by remember { mutableStateOf(false) }
    var isBPMHigh by remember { mutableStateOf(false) }

    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = ResultCardColor)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ResultSection()
                TimeDateSection(
                    creationTime = heartRate.creationTime,
                    creationDate = heartRate.creationDate
                )
            }
            BPMSection(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.12f),
                rate = heartRate.bpm.toFloat()
            )
            when (heartRate.bpm) {
                in 0..60 -> isBPMSlow = true
                in 60..100 -> isBPMNormal = true
                in 100..150 -> isBPMHigh = true
            }

            BPMIndicator(
                modifier = Modifier.fillMaxWidth(0.9f),
                title = UiKit.string.slow_bpm,
                range = UiKit.string.slow_bpm_range,
                color = ThirdBlue,
                isSelected = isBPMSlow
            )
            BPMIndicator(
                modifier = Modifier.fillMaxWidth(0.9f),
                title = UiKit.string.normal_bpm,
                range = UiKit.string.normal_bpm_range,
                color = SecondaryGreen,
                isSelected = isBPMNormal
            )
            BPMIndicator(
                modifier = Modifier.fillMaxWidth(0.9f),
                title = UiKit.string.high_bpm,
                range = UiKit.string.high_bpm_range,
                color = ThirdRed,
                isSelected = isBPMHigh
            )
        }
    }
}

@Composable
fun TimeDateSection(modifier: Modifier = Modifier, creationTime: Long, creationDate: Long) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = null,
            tint = CardGray
        )
        Column {
            Text(
                text = creationTime.toTime(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = CardGray
            )
            Text(
                text = creationDate.toDate(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = CardGray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BPMSection(modifier: Modifier = Modifier, rate: Float) {
    var animValue by remember { mutableFloatStateOf(0f) }
    val slideAnimation by animateFloatAsState(
        targetValue = animValue,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = ""
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.55f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                    .background(MainTurquoise)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(MainGreen)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                    .background(ThirdRed)
            )
        }
        Slider(
            modifier = Modifier.fillMaxWidth(),
            value = slideAnimation,
            onValueChange = {},
            valueRange = 0f..150f,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.Transparent,
                inactiveTrackColor = Color.Transparent
            ),
            thumb = {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.03f)
                        .background(Color.Gray, RoundedCornerShape(Dimens.dp_6)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .fillMaxHeight(0.9f)
                            .background(Color.White, RoundedCornerShape(Dimens.dp_6))
                    )
                }
            }
        )
    }

    LaunchedEffect(key1 = rate) {
        delay(300)
        animValue = rate
    }
}

@Composable
fun ResultSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(UiKit.string.your_result),
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = stringResource(UiKit.string.normal),
            style = MaterialTheme.typography.headlineSmall,
            color = MainGreen
        )
    }
}

@Composable
fun BPMIndicator(
    modifier: Modifier = Modifier,
    title: Int,
    range: Int,
    color: Color,
    isSelected: Boolean,
) {
    val textColor = if (isSelected) Color.Black else CardLightGray
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(0.45f),
            colors = CardDefaults.cardColors(containerColor = MainBlue),
            shape = RoundedCornerShape(15)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.dp_6),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .size(Dimens.dp_12)
                        .aspectRatio(1f)
                        .background(color, shape = CircleShape)
                )
                Text(
                    text = stringResource(title),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Text(
            text = stringResource(range),
            style = MaterialTheme.typography.bodyLarge,
            color = textColor
        )
    }
}


@Preview
@Composable
fun RCP() {
    HeartRateTheme {
        // ResultUIContent()
    }
}
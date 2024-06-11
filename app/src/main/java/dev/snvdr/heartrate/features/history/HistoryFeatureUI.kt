package dev.snvdr.heartrate.features.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.snvdr.common.ScrollBar
import dev.snvdr.common.Dimens
import dev.snvdr.common.DrawBackgroundEllipse
import dev.snvdr.common.toDate
import dev.snvdr.common.toTime
import dev.snvdr.heart.uikit.ui.theme.MainRed
import dev.snvdr.heart.uikit.ui.theme.SecondaryRed
import dev.snvdr.heart.uikit.ui.theme.ThirdRed
import dev.snvdr.heartrate.models.HeartRate
import dev.snvdr.heart.uikit.R as UiKit

@Composable
fun HistoryFeatureUI() {
    val viewModel: HistoryFeatureViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    HistoryScreenContent(
        heartRates = state.heartRates,
        onBackIconClick = { viewModel.onNavigateBack() },
        onClearClick = {
            viewModel.clearAllHeartRates()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreenContent(
    heartRates: List<HeartRate>,
    onBackIconClick: () -> Unit,
    onClearClick: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Scaffold(
        topBar = {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = MainRed),
                title = {
                    Text(
                        text = stringResource(UiKit.string.history),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Normal
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackIconClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPaddings ->
        DrawBackgroundEllipse()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPaddings),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val scrollState = rememberLazyListState()
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                state = scrollState,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(heartRates) {
                    BmpListItem(
                        modifier = Modifier
                            .height(screenHeight * 0.13f)
                            .animateItem(),
                        heartRate = it
                    )
                }
                if (heartRates.isNotEmpty()){
                    item {
                        Button(
                            modifier = Modifier.fillMaxWidth(0.9f),
                            colors = ButtonDefaults.textButtonColors(containerColor = MainRed),
                            onClick = { onClearClick() }) {
                            Text(text = stringResource(UiKit.string.clean_history), color = Color.White)
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxHeight(0.98f)
                    .clip(RoundedCornerShape(Dimens.dp_16)),
                border = BorderStroke(1.dp, ThirdRed),
                colors = CardDefaults.cardColors(containerColor = SecondaryRed),
            ) {
                ScrollBar(
                    state = scrollState,
                    modifier = Modifier
                        .fillMaxHeight()
                        .size(Dimens.dp_14)
                        .padding(Dimens.dp_2)
                )
            }
        }

    }
}

@Composable
fun BmpListItem(modifier: Modifier = Modifier, heartRate: HeartRate) {
    Card(
        modifier = modifier
            .fillMaxWidth(0.95f)
            .padding(vertical = Dimens.dp_8),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "${heartRate.bpm} BPM",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.W400
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Dimens.dp_16))
                    .background(MainRed)
                    .fillMaxHeight(0.85f)
                    .width(Dimens.dp_4)
            )
            Column {
                Text(
                    text = heartRate.creationTime.toTime(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
                Text(
                    text = heartRate.creationDate.toDate(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            }
        }
    }
}

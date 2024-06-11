package dev.snvdr.heartrate.features.loading

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.snvdr.common.DrawBackgroundEllipse
import dev.snvdr.common.LinearProgressIndicatorWithPercentage
import dev.snvdr.heart.uikit.R as UiKit

@Composable
fun LoadingUIFeatureUI() {

    val viewModel: LoadingFeatureViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LoadingUIFeatureContent(state = state)

}

@Composable
private fun LoadingUIFeatureContent(
    state: LoadingFeatureUIState,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Box(modifier = modifier.fillMaxSize()) {
        DrawBackgroundEllipse()
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.55f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight(0.5f),
                painter = painterResource(UiKit.drawable.ic_hearth),
                contentDescription = null
            )
            Text(
                text = stringResource(UiKit.string.heart_rate),
                style = MaterialTheme.typography.displayLarge
            )
        }
        LinearProgressIndicatorWithPercentage(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = screenHeight * 0.05f)
                .fillMaxWidth(0.9f),
            progress = state.progress,
            onLoadingEnd = {}
        )
    }
}
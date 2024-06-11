package dev.snvdr.heartrate.features.homepage.hp_1

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dev.snvdr.common.DrawBackgroundEllipse
import dev.snvdr.common.TopBarSection
import dev.snvdr.heart.uikit.ui.theme.MainRed
import dev.snvdr.heart.uikit.R as UiKit

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomePage1FeatureUI() {
    val viewModel: HomePage1FeatureViewModel = hiltViewModel()

    val cameraPermissionState =
        rememberPermissionState(permission = Manifest.permission.CAMERA) { status ->
            if (status) {
                viewModel.onNavigateToHomePage2()
            }
        }


    HomePage1FeatureContent(
        onHistoryClick = viewModel::onNavigateToHistory,
        onHeartClick = {
            if (cameraPermissionState.status.isGranted) {
                viewModel.onNavigateToHomePage2()
            } else {
                cameraPermissionState.launchPermissionRequest()
            }
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun HomePage1FeatureContent(
    modifier: Modifier = Modifier,
    onHistoryClick: () -> Unit,
    onHeartClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = MainRed),
                title = {},
                actions = {
                    TopBarSection(onHistoryClick = { onHistoryClick() })
                }
            )
        }
    ) { innerPaddings ->
        DrawBackgroundEllipse()
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPaddings)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.65f)
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = stringResource(UiKit.string.first_measurement),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Image(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .fillMaxHeight(0.55f),
                    painter = painterResource(UiKit.drawable.ic_hearth),
                    contentDescription = null
                )
            }
            Image(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.35f)
                    .fillMaxHeight(0.15f)
                    .clickable {
                        onHeartClick()
                    },
                painter = painterResource(UiKit.drawable.heart_btn),
                contentDescription = null
            )
        }
    }
}
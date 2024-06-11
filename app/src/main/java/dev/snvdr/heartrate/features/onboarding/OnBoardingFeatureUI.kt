package dev.snvdr.heartrate.features.onboarding

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.snvdr.common.Dimens
import dev.snvdr.common.DrawBackgroundEllipse
import dev.snvdr.heart.uikit.ui.theme.MainRed
import kotlinx.coroutines.launch
import dev.snvdr.heart.uikit.R as UiKit

@Composable
fun OnBoardingFeatureUI() {
    val viewModel:OnBoardingFeatureViewModel = hiltViewModel()

    OnBoardingFeatureContent(onBeginClick = viewModel::onNavigateToHomePage)
}

@Composable
private fun OnBoardingFeatureContent(onBeginClick: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { 3 })

    DrawBackgroundEllipse()
    Box(modifier = Modifier.fillMaxSize()) {
        OnBoardingPager(
            modifier = Modifier
                .fillMaxHeight(0.8f)
                .align(Alignment.TopCenter),
            pagerState = pagerState
        )
        OnBoardingFooter(
            modifier = Modifier.align(Alignment.BottomCenter),
            pagerState = pagerState,
            onBeginClick = {onBeginClick()}
        )
    }
}

@Composable
fun OnBoardingPager(modifier: Modifier = Modifier, pagerState: PagerState) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxWidth(0.95f)
    ) { page ->
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            OnBoardingPage(page = page)
        }
    }
}

@Composable
fun OnBoardingPage(modifier: Modifier = Modifier, page: Int) {
    val imageRes = when (page) {
        0 -> UiKit.drawable.onboarding_1
        1 -> UiKit.drawable.onboarding_2
        2 -> UiKit.drawable.onboarding_3
        else -> throw IllegalArgumentException("Invalid page index")
    }
    val stringsRes = when (page) {
        0 -> UiKit.string.onboarding_1_title to UiKit.string.onboarding_1_description
        1 -> UiKit.string.onboarding_2_title to UiKit.string.onboarding_2_description
        2 -> UiKit.string.onboarding_3_title to UiKit.string.onboarding_3_description
        else -> throw IllegalArgumentException("Invalid page index")
    }
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(0.6f),
            painter = painterResource(imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        TextHelper(
            title = stringsRes.first,
            description = stringsRes.second
        )
    }
}

@Composable
fun TextHelper(title: Int, description: Int) {
    Column(
        modifier = Modifier.fillMaxWidth(0.95f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = stringResource(description),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OnBoardingFooter(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    onBeginClick: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = screenHeight * 0.02f),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OnBoardingIndicators(
            modifier = Modifier.padding(Dimens.dp_10),
            pagerState = pagerState
        )
        Button(
            modifier = Modifier.fillMaxWidth(0.9f),
            onClick = {
                coroutineScope.launch {
                    if (pagerState.currentPage < pagerState.pageCount - 1) {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    } else {
                        onBeginClick()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MainRed)
        ) {
            Text(
                text = stringResource(
                    if (pagerState.currentPage == pagerState.pageCount - 1)
                        UiKit.string.begin
                    else
                        UiKit.string.continuee
                )
            )
        }
    }
}

@Composable
fun OnBoardingIndicators(modifier: Modifier = Modifier, pagerState: PagerState) {
    Row(
        modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val scale by animateFloatAsState(
                if (pagerState.currentPage == iteration) 1.25f else 1f,
                label = ""
            )
            if (pagerState.currentPage == iteration) {
                PageIndicator(
                    modifier = Modifier
                        .padding(horizontal = Dimens.dp_4)
                        .scale(scale)
                )
            } else {
                Box(
                    modifier = Modifier
                        .padding(horizontal = Dimens.dp_4)
                        .scale(scale)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                        .size(Dimens.dp_16)
                )
            }
        }
    }
}

@Composable
fun PageIndicator(
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(Dimens.dp_16))
            .background(MainRed)
            .height((screenHeight * 0.015f))
            .fillMaxWidth(0.1f)
    )
}
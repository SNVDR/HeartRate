package dev.snvdr.common

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.snvdr.heart.uikit.R
import dev.snvdr.heart.uikit.ui.theme.MainBlue
import dev.snvdr.heart.uikit.ui.theme.MainRed
import dev.snvdr.heart.uikit.ui.theme.SecondaryRed
import dev.snvdr.heart.uikit.ui.theme.ThirdRed

@Composable
fun LinearProgressIndicatorWithPercentage(
    modifier: Modifier = Modifier,
    progress: Float,
    onLoadingEnd: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val progressPercentage = (progress * 100).toInt()

    Card(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(Dimens.dp_16))
            .height((screenHeight * 0.025f)),
        border = BorderStroke(1.dp, ThirdRed),
        colors = CardDefaults.cardColors(containerColor = SecondaryRed),
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(Dimens.dp_16))
                .background(MainRed)
                .animateContentSize()
                .fillMaxHeight()
                .fillMaxWidth(progress)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = Dimens.dp_14),
                text = "$progressPercentage%",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }
    }

    LaunchedEffect(progress) {
        if (progress >= 1F) {
            onLoadingEnd()
        }
    }

}

@Composable
fun DrawBackgroundEllipse() {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Canvas(modifier = Modifier.fillMaxWidth()) {
            scale(scaleX = 25f, scaleY = 35f) {
                drawCircle(MainBlue, radius = (screenHeight * 0.023f).toPx())
            }
        }
    }

}

@Composable
fun TopBarSection(modifier: Modifier = Modifier, onHistoryClick: () -> Unit) {
    Row(
        modifier = modifier.clickable { onHistoryClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.history),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Normal
        )
        Icon(
            modifier = Modifier.size(Dimens.dp_40),
            imageVector = Icons.Default.History,
            contentDescription = null,
            tint = Color.White,
        )
    }
}

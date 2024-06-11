package dev.snvdr.common

import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.snvdr.heart.uikit.ui.theme.MainRed

@Composable
fun ScrollBar(
    state: LazyListState,
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0, to = 1.0, fromInclusive = false, toInclusive = false)
    minPercentage: Float = DefaultCarouselMinPercentage,
    @FloatRange(from = 0.0, to = 1.0, fromInclusive = false, toInclusive = false)
    maxPercentage: Float = DefaultCarouselMaxPercentage,
) {
    val itemLengthInPx = state.layoutInfo.visibleItemsInfo.firstOrNull()?.size ?: 0
    val totalLength = itemLengthInPx * state.layoutInfo.totalItemsCount
    val scrolled = state.firstVisibleItemIndex * itemLengthInPx + state.firstVisibleItemScrollOffset

    ScrollBarImpl(
        scrolled = scrolled,
        maxScroll = totalLength - state.layoutInfo.viewportEndOffset,
        totalLength = totalLength,
        modifier = modifier,
        minPercentage = minPercentage,
        maxPercentage = maxPercentage
    )
}

@Composable
private fun ScrollBarImpl(
    scrolled: Int,
    maxScroll: Int,
    totalLength: Int,
    modifier: Modifier,
    minPercentage: Float,
    maxPercentage: Float
) {
    require(minPercentage in 0f..1f) { "minPercentage should be > 0f and < 1f." }
    require(maxPercentage in 0f..1f) { "maxPercentage should be > 0f and < 1f." }

    if (totalLength <= 0 || maxScroll <= 0) return

    val layoutDirection = LocalLayoutDirection.current

    Canvas(modifier = modifier.size(DefaultCarouselWidth, DefaultCarouselHeight)) {
        val isLtr = layoutDirection == LayoutDirection.Ltr

        val width = drawContext.size.width
        val height = drawContext.size.height

        val isVertical = height > width
        val barLength = if (isVertical) height else width
        val barWidth = if (isVertical) width else height

        val viewportRatio = (totalLength - maxScroll) / totalLength.toFloat()
        val ratio = viewportRatio.coerceIn(minPercentage, maxPercentage)

        val thumbLength = ratio * barLength
        val maxScrollLength = barLength - thumbLength

        val xOffset: Float = (scrolled / maxScroll.toFloat()) * maxScrollLength
        val yOffset = barWidth / 2

        val barStart = if (isLtr) xOffset else maxScrollLength - xOffset
        val barEnd = barStart + thumbLength

        fun drawLine(
            startOffset: Float,
            endOffset: Float,
        ) = drawLine(
            color = MainRed,
            start = if (isVertical) Offset(yOffset, startOffset) else Offset(startOffset, yOffset),
            end = if (isVertical) Offset(yOffset, endOffset) else Offset(endOffset, yOffset),
            cap = StrokeCap.Round,
            strokeWidth = barWidth,
        )

        drawLine(barStart, barEnd)
    }
}

// Default values for the Carousel
const val DefaultCarouselMaxPercentage = 0.9f
const val DefaultCarouselMinPercentage = 0.2f
val DefaultCarouselWidth = 20.dp
val DefaultCarouselHeight = 4.dp

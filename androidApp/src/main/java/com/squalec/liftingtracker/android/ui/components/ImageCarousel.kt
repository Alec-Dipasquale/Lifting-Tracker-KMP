package com.squalec.liftingtracker.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp

@Composable
fun ImageCarousel(imageBitmaps: List<ImageBitmap?>) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { imageBitmaps.size })
    var shouldScroll by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = shouldScroll) {
        if (shouldScroll) {
            pagerState.animateScrollToPage(page = (pagerState.currentPage + 1) % imageBitmaps.size)
            shouldScroll = false
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    shouldScroll = !shouldScroll
                }
        ) { index ->
            ExerciseImage(drawable = imageBitmaps[index])
        }
        CarouselBubbleIndicators(
            imageBitmaps.size,
            pagerState.currentPage,
            Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun CarouselBubbleIndicators(
    maxCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        for (i in 0 until maxCount) {
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(
                        color = if (i == currentPage) Color.White else Color.Gray
                    )
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = CircleShape
                    )
                    .padding(4.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))

        }
    }
}
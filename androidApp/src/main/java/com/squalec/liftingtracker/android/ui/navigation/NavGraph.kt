package com.squalec.liftingtracker.android.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.wear.compose.material.Text
import com.squalec.liftingtracker.android.ui.ExerciseDetails.ExerciseDetailsIntent
import com.squalec.liftingtracker.android.ui.ExerciseDetails.ExerciseDetailsViewModel
import com.squalec.liftingtracker.android.ui.LoadingScreen
import com.squalec.liftingtracker.android.ui.ScreenExerciseSearch.ExerciseSearchScreen
import com.squalec.liftingtracker.android.ui.ScreenHome.HomeScreen
import com.squalec.liftingtracker.android.ui.themes.ExerciseDetailTheme
import com.squalec.liftingtracker.android.ui.themes.SearchExercisesTheme

@Composable
fun NavGraph(isLoading: Boolean) {

    val navController = rememberNavController()
    if (isLoading) {
        LoadingScreen()
        return
    }

    NavHost(
        navController = navController,
        startDestination = Destination.ExerciseSearchDestination
    ) {
        composable<Destination.LoadingDestination> {
            LoadingScreen()
        }
        composable<Destination.HomeDestination> {
            HomeScreen()
        }
        composable<Destination.ExerciseSearchDestination> {
            SearchExercisesTheme {
                ExerciseSearchScreen(navController = navController)
            }
        }
        composable<Destination.ExerciseDetailDestination> { backStackEntry ->
            ExerciseDetailTheme {
                val exerciseId = backStackEntry.toRoute<Destination.ExerciseDetailDestination>()
                ExerciseDetailScreen(exerciseId = exerciseId.exerciseId)
            }
        }
    }
}

@Composable
fun ExerciseDetailScreen(
    viewModel: ExerciseDetailsViewModel = viewModel(),
    exerciseId: String
) {

    val state by viewModel.state.collectAsState()
    LaunchedEffect(key1 = Unit) {
        viewModel.intent(ExerciseDetailsIntent.GetExercise(exerciseId))
    }
    LazyColumn(
        modifier = Modifier
            .scrollable(orientation = Orientation.Vertical, state = rememberScrollState())
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            Column {
                Text(
                    text = state.exerciseDetails?.name ?: "No Name",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                ImageCarouselv2(state.exerciseImageIds)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        item {
            ExerciseDetailListCard(
                title = "Primary Muscle",
                content = state.exerciseDetails?.primaryMuscles ?: listOf("No Primary Muscle Listed")
            )
        }
        item {
            ExerciseDetailListCard(
                title = "Secondary Muscle",
                content = state.exerciseDetails?.secondaryMuscles ?: listOf("No Secondary Muscle Listed")
            )
        }
        item {
            ExerciseDetailCard(
                title = "Equipment",
                content = state.exerciseDetails?.equipment ?: "No Equipment Listed"
            )
        }
        item {
            ExerciseDetailCard(
                title = "Mechanics",
                content = state.exerciseDetails?.mechanic ?: "No Mechanics Listed"
            )
        }
        item {
            ExerciseDetailCard(
                title = "Level",
                content = state.exerciseDetails?.level ?: "No Level Listed"
            )
        }
        item {
            ExerciseDetailCard(
                title = "Force",
                content = state.exerciseDetails?.force ?: "No Force Listed"
            )
        }
        item {
            ExerciseDetailCard(
                title = "Category",
                content = state.exerciseDetails?.category ?: "No Category Listed"
            )
        }
        item {
            ExerciseDetailListCard(
                title = "Instructions",
                content = state.exerciseDetails?.instructions ?: listOf("No Instructions Listed")
            )
        }
    }
}

@Composable
fun ExerciseDetailCard(title: String, content: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ExerciseDetailListCard(title: String, content: List<String>) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))

            if (content.isEmpty())
                Text(
                    text = "No $title Listed",
                    style = MaterialTheme.typography.bodyMedium
                )

            content.forEach {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun ExerciseImage(drawable: ImageBitmap?) {
    if (drawable != null) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f) // Adjust the aspect ratio as needed
                .clip(RoundedCornerShape(8.dp)),
            bitmap = drawable,
            contentDescription = null
        )
    } else {
        Text(text = "Image not found")
        // Handle the case where the image is not found
        // You can use a placeholder image or show an error message
    }
}

@Composable
fun ImageCarouselv2(imageBitmaps: List<ImageBitmap?>) {
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
package com.squalec.liftingtracker.android.ui.screenHome

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.squalec.liftingtracker.android.ui.navigation.Destination
import com.squalec.liftingtracker.android.ui.navigation.TabDestinations
import com.squalec.liftingtracker.android.ui.utilities.Icons
import com.squalec.liftingtracker.appdatabase.WorkoutSessionManager
import com.squalec.liftingtracker.utils.CustomDate

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            IconButton(

                onClick = {
                    navController.navigate(Destination.CalendarView)
                }) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = Icons.Custom.MyHistory(),
                    contentDescription = ""
                )
            }
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .clickable {
                        val date = if (WorkoutSessionManager.workoutState.value.isWorkoutInProgress)
                            null else CustomDate.now().utcDate
                        navController.navigate(Destination.WorkoutSession(date = date))
                    }
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp)
                    .border(4.dp, MaterialTheme.colorScheme.secondary, CircleShape)
                    .padding(4.dp)
                    .align(Alignment.Center)
                ,
                contentAlignment = Alignment.Center) {
                Text(
                    text = "Start\n Workout",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center
                    ),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    //todo implement bottom navigation bar
}

@Composable
fun Navigations(navController: NavController) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destination.Home
    ) {
        composable<TabDestinations.Main> {

        }
    }
}
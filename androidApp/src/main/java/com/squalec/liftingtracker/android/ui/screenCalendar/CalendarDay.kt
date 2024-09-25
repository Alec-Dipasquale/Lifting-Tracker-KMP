package com.squalec.liftingtracker.android.ui.screenCalendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.squalec.liftingtracker.android.ui.themes.CalendarViewTheme
import com.squalec.liftingtracker.appdatabase.repositories.WorkoutSessionModel
import com.squalec.liftingtracker.utils.CustomDate
import java.time.LocalDate

@Composable
fun Day(
    day: CalendarDay, workoutSessions: List<WorkoutSessionModel>,
    onClick: () -> Unit = { }
) {
    Box(
        modifier = Modifier
            .padding(2.dp)
            .aspectRatio(0.5f)
            .border(1.dp, MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.medium)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {
                onClick()
            }
    ) {
        Column(
            modifier = Modifier
                .padding(2.dp)
                .matchParentSize()
        ) {
            Row {
                Text(
                    text = day.date.dayOfMonth.toString(),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleSmall,  // Larger text for the date
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.End
                )

            }
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(vertical = 4.dp)
                ) {
                    items(workoutSessions) { session ->
                        WorkoutSessionView(session)  // Assuming this is a custom composable for sessions
                    }
                }
            }
        }
    }
}

@Composable
fun WorkoutSessionView(session: WorkoutSessionModel) {
    Row {
        Icon(
            Icons.Default.Done,
            contentDescription = "",
            tint = Color.Green,
            modifier = Modifier
                .padding(1.dp)
                .size(8.dp)
                .align(Alignment.CenterVertically)
        )
        Text(
            text = session.workoutName,
            fontSize = 8.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis  // Use ellipsis for longer text
        )
    }
}


@Preview
@Composable
fun DayPreview() {
    CalendarViewTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth(1 / 7f)
            ) {
                Day(
                    day = CalendarDay(LocalDate.now(), DayPosition.MonthDate),
                    workoutSessions = listOf(
                        WorkoutSessionModel(
                            "Workout 1",
                            exercises = emptyList(),
                            caloriesBurned = 0,
                            duration = 0,
                            date = CustomDate.now()
                        ),
                        WorkoutSessionModel(
                            "Workout 2",
                            exercises = emptyList(),
                            caloriesBurned = 0,
                            duration = 0,
                            date = CustomDate.now()
                        ),
                        WorkoutSessionModel(
                            "Workout 3",
                            exercises = emptyList(),
                            caloriesBurned = 0,
                            duration = 0,
                            date = CustomDate.now()
                        ),

                        )
                )
            }
        }
    }
}
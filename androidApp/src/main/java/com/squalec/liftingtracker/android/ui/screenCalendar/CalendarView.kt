package com.squalec.liftingtracker.android.ui.screenCalendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.squalec.liftingtracker.android.ui.components.CalendarWorkoutsDialogue
import com.squalec.liftingtracker.android.ui.themes.CalendarViewTheme
import com.squalec.liftingtracker.appdatabase.models.ExerciseDetails
import com.squalec.liftingtracker.appdatabase.repositories.ExerciseSessionModel
import com.squalec.liftingtracker.appdatabase.repositories.WorkoutSessionModel
import com.squalec.liftingtracker.utils.CustomDate
import com.squalec.liftingtracker.utils.CustomDateRange
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber
import java.time.DayOfWeek
import java.time.YearMonth

@Composable
fun CalendarView(
    viewModel: CalendarViewModel = koinViewModel(),
    navController: NavController
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(24) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(24) } // Adjust as needed
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library
    Timber.d("Current month: $currentMonth and currentmonth: ${currentMonth.month.value}")

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.intent(
            CalendarIntent.GetWorkoutSessions(
                CustomDateRange(
                    CustomDate.setMonthFromInt(currentMonth.month.value),
                    CustomDateRange.DateRange.MONTH,
                )
            )
        )
    }

    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    CalendarViewContent(
        calendarState,
        state,
        onIntent = { day, workoutSessions ->
            viewModel.intent(
                CalendarIntent.OnDaySelected(
                    day.formatToCustomDate(),
                    workoutSessions
                )
            )
        })


    if (state.calendarWorkoutDialogueState.isWorkoutsDialogueOpen) {
        CalendarWorkoutsDialogue(
            state.calendarWorkoutDialogueState,
            navController = navController,
            onIntent = { intent ->
                viewModel.intent(intent)
            }
        )
    }

}


@Composable
fun CalendarViewContent(
    calendarState: com.kizitonwose.calendar.compose.CalendarState, state: CalendarState,
    onIntent: (CalendarDay, List<WorkoutSessionModel>) -> Unit = { _, _ -> }
) {
    HorizontalCalendar(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        monthHeader = { month ->

            val daysOfWeek = month.weekDays.first().map { it.date.dayOfWeek }
            MonthHeader(daysOfWeek = daysOfWeek, month = month.yearMonth)
        },

        state = calendarState,
        dayContent = { day ->
            val workoutSessions = state.workoutSessions.filter { workoutSessionModel ->

                workoutSessionModel.date.formattedToDay() == day.formatToCustomDate()
                    .formattedToDay()
            }
            Day(
                day = day,
                workoutSessions = workoutSessions,
                onClick = {
                    onIntent(day, workoutSessions)
                }
            )
        }
    )
}

@Composable
fun MonthHeader(daysOfWeek: List<DayOfWeek>, month: YearMonth) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Text(
            text = month.month.name,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            daysOfWeek.forEach {
                Text(
                    text = it.name.take(3),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }

}

@Preview
@Composable
fun PreviewCalendarView() {
    CalendarViewTheme {
        CalendarViewContent(
            calendarState = rememberCalendarState(),
            state = CalendarState()
        )
    }
}

fun CalendarDay.formatToCustomDate(): CustomDate {
    // Convert LocalDate to "yyyy-MM-dd" and append "T00:00:00" to form the expected format
    val dateString = this.date.toString() + "T00:00:00"
    return CustomDate(dateString)
}
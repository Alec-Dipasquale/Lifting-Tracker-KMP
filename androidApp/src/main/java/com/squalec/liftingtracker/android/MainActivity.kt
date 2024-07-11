package com.squalec.liftingtracker.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.squalec.liftingtracker.appdatabase.AppDatabase
import com.squalec.liftingtracker.appdatabase.DBFactory
import com.squalec.liftingtracker.appdatabase.populateExerciseDetailsDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.context.startKoin
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d("Hi, I am Main Class")

        val db = DBFactory.createDatabase()
        CoroutineScope(context = Dispatchers.IO).launch {
            populateExerciseDetailsDatabase()
            Timber.d("[Database123] Database populated")
        }

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 16.dp),
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,

                        ) {
                        var text by remember { mutableStateOf("") }
                        TextField(value = text, onValueChange = {
                            text = it
                            searchExerciseDatabase(db, text)
                        })

                    }
                }
            }
        }
    }

    fun searchExerciseDatabase(db: AppDatabase, text: String) {
        CoroutineScope(context = Dispatchers.IO).launch {
            val exerciseDetails = db.exerciseDao().searchExercises(text)
            exerciseDetails.forEach {
                Timber.d("[Database123] Exercise: $it")
            }
            Timber.d("[Database123] Search completed")
        }
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}

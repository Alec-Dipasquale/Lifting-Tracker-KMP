package com.squalec.liftingtracker.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.squalec.liftingtracker.android.ui.navigation.NavGraph
import com.squalec.liftingtracker.appdatabase.DBFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isLoading by remember {
                mutableStateOf(true)
            }
            val dbFactory = DBFactory
            CoroutineScope(context = Dispatchers.IO).launch {
                dbFactory.populateExerciseDetailsDatabase()
                isLoading = false
                Timber.d("[Database123] Database populated")
            }
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(isLoading = isLoading)
                }
            }
        }
    }


}

package dev.snvdr.heartrate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.snvdr.heart.uikit.ui.theme.HeartRateTheme
import dev.snvdr.heartrate.features.navigation.graph.NavigationGraph
import dev.snvdr.heartrate.features.navigation.utils.NavigationEffects

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HeartRateTheme {
                val navController = rememberNavController()
                val mainViewModel: MainViewModel = hiltViewModel()

                NavigationEffects(
                    navigationChannel = mainViewModel.navigationChannel,
                    navHostController = navController
                )

                NavigationGraph(navHostController = navController)
            }
        }
    }
}

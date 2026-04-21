package vadimerenkov.autasker.androidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import vadimerenkov.autasker.common.settings.Settings
import vadimerenkov.autasker.navigation.AutaskerApp
import vadimerenkov.autasker.navigation.RootNavDisplay

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
	lateinit var settings: Settings

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
	        settings = koinInject<Settings>()
	        AutaskerApp(settings) {
		        RootNavDisplay()
	        }
        }
    }

	override fun onPause() {
		super.onPause()
		GlobalScope.launch {
			settings.saveExitTime()
		}
	}
}
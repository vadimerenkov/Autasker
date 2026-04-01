package vadimerenkov.autasker.presentation.windows

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.Notification
import androidx.compose.ui.window.TrayState
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import androidx.datastore.preferences.core.Preferences
import autasker.composeapp.generated.resources.Res
import autasker.composeapp.generated.resources.app_name
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.autasker.presentation.navigation.RootNavDisplay
import vadimerenkov.autasker.settings.Settings
import vadimerenkov.autasker.settings.restoreWindowState
import vadimerenkov.autasker.settings.saveWindowState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainWindow(
	isOpen: Boolean,
	trayState: TrayState,
	settings: Settings,
	preferences: Preferences,
	onExitRequest: () -> Unit,
	toTrayRequest: () -> Unit,
) {

	val scope = rememberCoroutineScope()
	val windowState = remember { preferences.restoreWindowState() } ?: rememberWindowState()

	if (isOpen) {
		Window(
			state = windowState,
			onCloseRequest = {
				scope.launch {
					settings.saveWindowState(windowState)
					settings.saveExitTime()

					if (settings.state.closeToTray) {
						toTrayRequest()
					} else {
						onExitRequest()
					}
				}
			},
			title = stringResource(Res.string.app_name)
		) {
			RootNavDisplay(
				sendNotification = {
					val notification = Notification("This is a title", "This is a message")
					trayState.sendNotification(notification)
				}
			)
		}
	}
}
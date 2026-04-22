package vadimerenkov.autasker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import androidx.datastore.preferences.core.Preferences
import autasker.common.generated.resources.Res
import autasker.common.generated.resources.app_icon
import autasker.common.generated.resources.app_name
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.autasker.core.domain.settings.Settings
import vadimerenkov.autasker.core.presentation.settings.restoreWindowState
import vadimerenkov.autasker.core.presentation.settings.saveWindowState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainWindow(
	isOpen: Boolean,
	settings: Settings,
	preferences: Preferences,
	onExitRequest: () -> Unit,
	toTrayRequest: () -> Unit,
) {

	val scope = rememberCoroutineScope()
	val windowState = remember { preferences.restoreWindowState() } ?: rememberWindowState()

	if (isOpen) {
		Window(
			icon = painterResource(Res.drawable.app_icon),
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
			RootNavDisplay(settings)
		}
	}
}
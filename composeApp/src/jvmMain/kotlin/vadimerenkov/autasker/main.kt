package vadimerenkov.autasker

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Notification
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberTrayState
import autasker.common.generated.resources.Res
import autasker.common.generated.resources.app_icon
import autasker.common.generated.resources.exit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.context.GlobalContext.get
import vadimerenkov.autasker.common.domain.reminders.QuartzScheduler
import vadimerenkov.autasker.common.presentation.util.SuspendDateFormatter
import vadimerenkov.autasker.common.settings.Settings
import vadimerenkov.autasker.di.initKoin
import vadimerenkov.autasker.navigation.AutaskerApp
import vadimerenkov.autasker.navigation.MainWindow

fun main() {

	initKoin()
	val settings: Settings = get().get()
	val notificationCenter: QuartzScheduler = get().get()
	val data = runBlocking { settings.dataStore.data.first() }

	System.setProperty("skiko.renderApi", "OPENGL")

	application {
		val trayState = rememberTrayState()
		var isOpen by remember { mutableStateOf(true) }


		LaunchedEffect(notificationCenter.events) {
			withContext(Dispatchers.Main.immediate) {
				notificationCenter.events.collect { task ->
					val notification = Notification(
						title = task.title,
						message = SuspendDateFormatter.formatDuration( task.dueDate!!, task.isAllDay)
					)
					println(notification)
					trayState.sendNotification(notification)
				}
			}
		}

		Tray(
			icon = painterResource(Res.drawable.app_icon),
			state = trayState,
			onAction = {
				isOpen = true
			},
			menu = {
				Item(
					text = stringResource(Res.string.exit),
					onClick = {
						exitApplication()
					}
				)
			}
		)
		AutaskerApp(settings) {

			MainWindow(
				isOpen = isOpen,
				settings = settings,
				preferences = data,
				toTrayRequest = {
					isOpen = false
				},
				onExitRequest = ::exitApplication
			)
		}
	}
}
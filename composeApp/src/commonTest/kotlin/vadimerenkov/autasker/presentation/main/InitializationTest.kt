package vadimerenkov.autasker.presentation.main

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import vadimerenkov.autasker.domain.Period
import vadimerenkov.autasker.domain.Task
import vadimerenkov.autasker.domain.TaskCategory
import vadimerenkov.autasker.domain.reminders.Reminder
import vadimerenkov.autasker.domain.reminders.ReminderService
import vadimerenkov.autasker.domain.roundToMinutes
import vadimerenkov.autasker.fakes.FakeAudioPlayer
import vadimerenkov.autasker.fakes.FakeReminderService
import vadimerenkov.autasker.fakes.TasksRepositoryFake
import vadimerenkov.autasker.presentation.task_edit.TaskEditViewModel
import vadimerenkov.autasker.settings.Settings
import vadimerenkov.autasker.settings.createDataStore
import java.io.File
import java.time.ZonedDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class InitializationTest {
	private lateinit var repository: TasksRepositoryFake
	private lateinit var settings: Settings
	private lateinit var dataStore: DataStore<Preferences>
	private lateinit var testDispatcher: TestDispatcher
	private lateinit var applicationScope: CoroutineScope
	private lateinit var reminderService: ReminderService

	@Before
	fun setUp() {
		testDispatcher = StandardTestDispatcher()
		Dispatchers.setMain(testDispatcher)
		repository = TasksRepositoryFake()
		applicationScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
		reminderService = FakeReminderService(repository)
		dataStore = createDataStore {
			val file = File(System.getProperty("java.io.tmpdir"), "test_datastore.preferences_pb")
			file.absolutePath
		}
		settings = Settings(
			dataStore = dataStore,
			applicationScope = applicationScope
		)

	}

	@After
	fun tearDown() {
		applicationScope.launch {
			dataStore.edit { it.clear() }
		}
		File(System.getProperty("java.io.tmpdir"), "test_datastore.preferences_pb").delete()
		Dispatchers.resetMain()
		applicationScope.cancel()
	}

	@Test
	fun `Deleted tasks are deleted permanently after 30 days`() = runBlocking() {
		val task = Task(
			title = "Task to delete",
			isDeleted = true,
			deletedDate = ZonedDateTime.now().minusDays(35)
		)
		repository.saveTask(task)
		assertThat(repository.tasks.value).contains(task)
		val viewModel = MainViewModel(repository, reminderService, FakeAudioPlayer(), settings, mutableListOf())
		testDispatcher.scheduler.advanceUntilIdle()

		assertThat(repository.tasks.value).doesNotContain(task)
	}

	@Test
	fun `Completed tasks are deleted after 7 days`() = runBlocking {
		testDispatcher.scheduler.advanceUntilIdle()
		val task = Task(
			id = 123,
			title = "Task to delete",
			isCompleted = true,
			completedDate = ZonedDateTime.now().minusDays(8)
		)
		repository.saveTask(task)
		assertThat(repository.tasks.value).contains(task)
		val viewModel = MainViewModel(repository, reminderService, FakeAudioPlayer(), settings, mutableListOf())
		testDispatcher.scheduler.advanceUntilIdle()

		assertThat(repository.tasks.value).contains(task.copy(isDeleted = true, deletedDate = ZonedDateTime.now().roundToMinutes()))
	}

	@Test
	fun `Task is loaded, reminders are loaded too`() = runBlocking {
		val task = Task(
			id = 123,
			categoryId = 3,
			title = "Task with reminders",
		)
		val reminder = Reminder(
			parentTaskId = task.id,
			period = Period.WEEK,
			times = 3
		)
		val category = TaskCategory(
			id = 3,
			isDefault = true,
			index = 3
		)
		repository.saveCategory(category)
		repository.saveTask(task)
		repository.saveReminders(listOf(reminder))
		testDispatcher.scheduler.advanceUntilIdle()
		val viewModel = TaskEditViewModel(
			id = task.id,
			categoryId = task.categoryId,
			repository = repository,
			reminderService = reminderService
		)
		testDispatcher.scheduler.advanceUntilIdle()
		assertThat(viewModel.state.reminders).contains(reminder)
	}
}
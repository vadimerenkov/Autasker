package vadimerenkov.autasker.presentation.new_day

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isNotEmpty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.koin.core.KoinApplication
import org.koin.test.KoinTestRule
import vadimerenkov.autasker.common.di.commonModule
import vadimerenkov.autasker.common.di.commonPlatformModule
import vadimerenkov.autasker.common.domain.RepeatState
import vadimerenkov.autasker.common.domain.Task
import vadimerenkov.autasker.common.domain.reminders.Reminder
import vadimerenkov.autasker.common.presentation.new_day.NewDayAction
import vadimerenkov.autasker.common.presentation.new_day.NewDayViewModel
import vadimerenkov.autasker.common.settings.Settings
import vadimerenkov.autasker.fakes.FakeReminderService
import vadimerenkov.autasker.fakes.TasksRepositoryFake
import java.time.ZonedDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class NewDayViewModelTest {

	@get:Rule
	val koinTestRule = KoinTestRule.create {
		// Your KoinApplication instance here
		KoinApplication.init().modules(commonModule, commonPlatformModule)
	}

	@get:Rule
	val temporaryFolder: TemporaryFolder = TemporaryFolder.builder()
		.assureDeletion()
		.build()

	private lateinit var viewModel: NewDayViewModel
	private lateinit var repository: TasksRepositoryFake
	private lateinit var settings: Settings
	private lateinit var applicationScope: CoroutineScope
	private lateinit var testDispatcher: TestDispatcher

	suspend fun initialize() {
		val task1 = Task(
			id = 123,
			title = "Test task",
			isCompleted = true,
			dueDate = ZonedDateTime.now(),
			repeatState = RepeatState(
				isRepeating = true
			),
			reminders = listOf(
				Reminder()
			)
		)
		val task2 = Task(
			id = 321,
			title = "Test task 2"
		)
		repository.saveTasks(listOf(task1, task2))
	}

	@Before
	fun setUp() = runBlocking {
		testDispatcher = StandardTestDispatcher()
		Dispatchers.setMain(testDispatcher)
		applicationScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
		val dataStore = PreferenceDataStoreFactory.create(
			scope = applicationScope,
			produceFile = {
				temporaryFolder.newFile("test_datastore.preferences_pb")
			}
		)
		settings = Settings(
			dataStore = dataStore,
			applicationScope = applicationScope
		)
		repository = TasksRepositoryFake()

		initialize()

		viewModel = NewDayViewModel(
			repository = repository,
			settings = settings,
			backStack = mutableListOf(),
			reminderService = FakeReminderService(repository)
		)
	}

	@Test
	fun `finishing the task reschedules reminders`() = runBlocking {
		assertThat(repository.jobs.value).isEmpty()
		viewModel.onAction(NewDayAction.NextButtonClick)
		testDispatcher.scheduler.advanceUntilIdle()
		assertThat(repository.jobs.value).isNotEmpty()
	}
}
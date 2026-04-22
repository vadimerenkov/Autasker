package vadimerenkov.autasker.presentation.main

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.koin.core.KoinApplication
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import vadimerenkov.autasker.calendar.calendarModule
import vadimerenkov.autasker.core.database.di.coreDatabaseModule
import vadimerenkov.autasker.core.database.di.platformCoreDatabaseModule
import vadimerenkov.autasker.core.domain.Page
import vadimerenkov.autasker.core.domain.ReminderJob
import vadimerenkov.autasker.core.domain.Subtask
import vadimerenkov.autasker.core.domain.Task
import vadimerenkov.autasker.core.domain.TaskCategory
import vadimerenkov.autasker.core.domain.Time
import vadimerenkov.autasker.core.domain.di.coreDomainModule
import vadimerenkov.autasker.core.domain.di.platformCoreDomainModule
import vadimerenkov.autasker.core.domain.reminders.ReminderService
import vadimerenkov.autasker.core.domain.settings.Settings
import vadimerenkov.autasker.core.presentation.di.corePresentationModule
import vadimerenkov.autasker.core.presentation.di.platformCorePresentationModule
import vadimerenkov.autasker.core.presentation.main.MainAction
import vadimerenkov.autasker.core.presentation.main.MainViewModel
import vadimerenkov.autasker.fakes.FakeAudioPlayer
import vadimerenkov.autasker.fakes.FakeReminderService
import vadimerenkov.autasker.fakes.TasksRepositoryFake
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest: KoinTest {

	@get:Rule
	val koinTestRule = KoinTestRule.create {
		// Your KoinApplication instance here
		KoinApplication.init().modules(
			coreDatabaseModule,
			platformCoreDatabaseModule,
			coreDomainModule,
			platformCoreDomainModule,
			corePresentationModule,
			platformCorePresentationModule,
			calendarModule
		)
	}

	@get:Rule
	val temporaryFolder: TemporaryFolder = TemporaryFolder.builder()
		.assureDeletion()
		.build()

	private lateinit var viewModel: MainViewModel
	private lateinit var repository: TasksRepositoryFake
	private lateinit var dataStore: DataStore<Preferences>
	private lateinit var applicationScope: CoroutineScope
	private lateinit var reminderService: ReminderService
	private lateinit var settings: Settings
	private lateinit var testDispatcher: TestDispatcher


	@Before
	fun setUp() {
		testDispatcher = StandardTestDispatcher()
		Dispatchers.setMain(testDispatcher)
		applicationScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
		repository = TasksRepositoryFake()
		reminderService = FakeReminderService(repository)

		dataStore = PreferenceDataStoreFactory.create(
			scope = applicationScope,
			produceFile = {
				temporaryFolder.newFile("test_datastore.preferences_pb")
			}
		)

		settings = Settings(
			dataStore = dataStore,
			applicationScope = applicationScope
		)

		viewModel = MainViewModel(
			repository = repository,
			reminderService = reminderService,
			settings = settings,
			audioPlayer = FakeAudioPlayer()
		)
	}

	@After
	fun tearDown() {
		File(System.getProperty("java.io.tmpdir"), "test_datastore.preferences_pb").delete()
		testDispatcher.scheduler.advanceUntilIdle()
		applicationScope.cancel()
		Dispatchers.resetMain()
	}

	@Test
	fun `Complete task, task is completed`() = runBlocking {
		val task = Task(
			id = 123,
			title = "TestTask",
			categoryId = 1
		)
		repository.saveTask(task)
		testDispatcher.scheduler.advanceUntilIdle()

		viewModel.onAction(MainAction.CheckmarkToggle(task.id, true))
		testDispatcher.scheduler.advanceUntilIdle()
		assertThat(repository.tasks.value.any { it.id == task.id && it.isCompleted }).isTrue()
	}

	@Test
	fun `UnComplete task, task is uncompleted`() = runBlocking {
		val task = Task(
			id = 123,
			title = "TestTask",
			isCompleted = true,
			completedDate = ZonedDateTime.now(),
			categoryId = 1
		)
		repository.saveTask(task)
		testDispatcher.scheduler.advanceUntilIdle()

		viewModel.onAction(MainAction.CheckmarkToggle(task.id, false))
		testDispatcher.scheduler.advanceUntilIdle()
		assertThat(repository.tasks.value.any { it.id == task.id && !it.isCompleted && it.completedDate == null }).isTrue()
	}

	@Test
	fun `Task is completed, jobs are deleted`() = runBlocking {
		val task = Task(
			id = 123,
			title = "TestTask",
			categoryId = 1
		)
		val job = ReminderJob(
			key = "123",
			parentTaskId = 123,
			triggerDate = Instant.now()
		)
		repository.saveJob(job)
		assertThat(repository.jobs.value).contains(job)
		repository.saveTask(task)
		testDispatcher.scheduler.advanceUntilIdle()

		viewModel.onAction(MainAction.CheckmarkToggle(task.id, true))
		testDispatcher.scheduler.advanceUntilIdle()
		assertThat(repository.jobs.value).isEmpty()
	}

	@Test
	fun `New column button clicked, new category is added`() = runBlocking {
		val page = Page(id = 123)
		repository.savePage(page)
		testDispatcher.scheduler.advanceUntilIdle()
		assertThat(repository.categories.value.size).isEqualTo(1)
		viewModel.onAction(MainAction.NewColumnClick)
		testDispatcher.scheduler.advanceUntilIdle()
		assertThat(repository.categories.value.size).isEqualTo(2)
	}

	@Test
	fun `Setting column as default sets it as default`() = runBlocking {
		val category = TaskCategory(
			id = 2,
			title = "test category",
			index = 3
		)
		repository.saveCategory(category)
		testDispatcher.scheduler.advanceUntilIdle()
		viewModel.onAction(MainAction.SetColumnDefault(category.id))
		testDispatcher.scheduler.advanceUntilIdle()
		assertThat(repository.categories.value).contains(category.copy(isDefault = true))
	}

	@Test
	fun `Changing column name changes name`() = runBlocking {
		val category = TaskCategory(
			id = 2,
			title = "test category",
			index = 3
		)
		val newTitle = "new test title"
		repository.saveCategory(category)
		testDispatcher.scheduler.advanceUntilIdle()
		viewModel.onAction(MainAction.ChangeColumnTitle(category.id, newTitle))
		testDispatcher.scheduler.advanceUntilIdle()
		assertThat(repository.categories.value).contains(category.copy(title = newTitle))
	}

	@Test
	fun `Deleting task moves it to trash`() = runBlocking {
		val task = Task(
			id = 123,
			categoryId = 1,
			title = "test title"
		)
		repository.saveTask(task)
		testDispatcher.scheduler.advanceUntilIdle()
		viewModel.onAction(MainAction.DeleteTask(task.id))
		testDispatcher.scheduler.advanceUntilIdle()
		assertThat(repository.tasks.value.any { it.id == task.id && it.isDeleted }).isTrue()
	}

	@Test
	fun `Completing all subtasks completes the task`() = runBlocking {
		val task = Task(
			id = 123,
			categoryId = 1,
			title = "test title"
		)
		val subtasks = listOf(
			Subtask(
				id = 1,
				parentTaskId = task.id,
				title = "test subtask",
				index = 1
			),
			Subtask(
				id = 2,
				parentTaskId = task.id,
				title = "test subtask2",
				index = 2
			)
		)
		repository.saveTask(task)
		repository.saveSubtasks(subtasks)
		testDispatcher.scheduler.advanceUntilIdle()
		viewModel.onAction(MainAction.SubtaskToggle(task.id, 1))
		viewModel.onAction(MainAction.SubtaskToggle(task.id, 2))
		testDispatcher.scheduler.advanceUntilIdle()
		assertThat(repository.tasks.value.any { it.id == task.id && it.isCompleted }).isTrue()
	}

	@Test
	fun `Set for today sets for today`() = runBlocking {
		val task = Task(
			id = 123,
			categoryId = 1,
			title = "test title"
		)
		repository.saveTask(task)
		testDispatcher.scheduler.advanceUntilIdle()
		viewModel.onAction(MainAction.SetForToday(task.id))
		testDispatcher.scheduler.advanceUntilIdle()
		assertThat(repository.tasks.value).contains(task.copy(dueDate = Time.today().atTime(12, 0).atZone(ZoneId.systemDefault())))
	}

	@Test
	fun `Set for tomorrow sets for tomorrow`() = runBlocking {
		val task = Task(
			id = 123,
			categoryId = 1,
			title = "test title"
		)
		repository.saveTask(task)
		testDispatcher.scheduler.advanceUntilIdle()
		viewModel.onAction(MainAction.SetForTomorrow(task.id))
		testDispatcher.scheduler.advanceUntilIdle()
		assertThat(repository.tasks.value).contains(task.copy(dueDate = Time.tomorrow().atTime(12, 0).atZone(ZoneId.systemDefault())))
	}

}
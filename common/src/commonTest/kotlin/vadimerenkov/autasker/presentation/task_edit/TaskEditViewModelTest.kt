package vadimerenkov.autasker.presentation.task_edit

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import assertk.assertions.isTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import vadimerenkov.autasker.common.domain.Period
import vadimerenkov.autasker.common.domain.Subtask
import vadimerenkov.autasker.common.domain.Task
import vadimerenkov.autasker.common.domain.reminders.Reminder
import vadimerenkov.autasker.common.presentation.task_edit.TaskEditAction
import vadimerenkov.autasker.common.presentation.task_edit.TaskEditViewModel
import vadimerenkov.autasker.fakes.FakeReminderService
import vadimerenkov.autasker.fakes.TasksRepositoryFake

@OptIn(ExperimentalCoroutinesApi::class)
class TaskEditViewModelTest {

	private lateinit var viewModel: TaskEditViewModel
	private lateinit var repository: TasksRepositoryFake
	private lateinit var testDispatcher: TestDispatcher

	@Before
	fun setUp() {
		testDispatcher = StandardTestDispatcher()
		Dispatchers.setMain(testDispatcher)
		repository = TasksRepositoryFake()
		val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
		viewModel = TaskEditViewModel(
			id = null,
			categoryId = 1,
			repository = repository,
			reminderService = FakeReminderService(repository)
		)
	}

	@After
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun `Saving task, task gets saved`() = runBlocking {
		val task = Task(
			title = "TestTask"
		)
		viewModel.onAction(TaskEditAction.TitleChange(task.title))
		viewModel.saveTask()
		testDispatcher.scheduler.advanceUntilIdle()
		assertThat(repository.tasks.value).contains(task)
	}

	@Test
	fun `Saving task, subtasks get saved`() = runBlocking {
		val task = Task(
			title = "TestTask",
		)
		val subtask = Subtask(
			parentTaskId = task.id,
			title = "SubtaskTest",
			index = 1
		)
		viewModel.onAction(TaskEditAction.TitleChange(task.title))
		viewModel.onAction(TaskEditAction.SaveSubtask(subtask.title))
		viewModel.saveTask()
		assertThat(repository.subtasks.value).contains(subtask)
	}

	@Test
	fun `Saving task, reminders get saved`() = runBlocking {
		val task = Task(
			title = "TestTask",
		)
		val reminder = Reminder(
			period = Period.WEEK,
			times = 3,
			parentTaskId = task.id
		)
		viewModel.onAction(TaskEditAction.TitleChange(task.title))
		viewModel.onAction(TaskEditAction.AddReminder(reminder))
		viewModel.onAction(TaskEditAction.ConfirmCalendarChanges)
		viewModel.saveTask()
		assertThat(repository.reminders.value).contains(reminder)
	}

	@Test
	fun `Saving task, previous reminders are deleted`() = runBlocking {
		val task = Task(
			title = "TestTask",
		)
		val reminder = Reminder(
			period = Period.WEEK,
			times = 3,
			parentTaskId = task.id
		)
		repository.saveReminders(listOf(reminder))
		assertThat(repository.reminders.value).contains(reminder)
		viewModel.onAction(TaskEditAction.TitleChange(task.title))
		viewModel.saveTask()
		assertThat(repository.reminders.value).doesNotContain(reminder)
	}

	@Test
	fun `Saving task, previous subtasks are deleted`() = runBlocking {
		val task = Task(
			title = "TestTask",
		)
		val subtask = Subtask(
			parentTaskId = task.id,
			title = "SubtaskTest",
			index = 1
		)
		repository.saveTask(task)
		repository.saveSubtasks(listOf(subtask))

		viewModel.onAction(TaskEditAction.TitleChange(task.title))
		viewModel.saveTask()
		assertThat(repository.subtasks.value).doesNotContain(subtask)
	}

	@Test
	fun `Minute is selected, check time off, period is reset to null`() {
		viewModel.onAction(TaskEditAction.HasTimeToggle(true))
		viewModel.onAction(TaskEditAction.PeriodChange(Period.MINUTE))
		assertThat(viewModel.dateTimeState.period).isEqualTo(Period.MINUTE)
		viewModel.onAction(TaskEditAction.HasTimeToggle(false))
		assertThat(viewModel.dateTimeState.period).isNull()
	}

	@Test
	fun `Saving task, jobs are saved`() = runBlocking {
		val task = Task(
			title = "TestTask",
		)
		val reminder = Reminder(
			period = Period.WEEK,
			times = 3,
			parentTaskId = task.id
		)
		viewModel.onAction(TaskEditAction.TitleChange(task.title))
		viewModel.onAction(TaskEditAction.AddReminder(reminder))
		viewModel.onAction(TaskEditAction.ConfirmCalendarChanges)
		viewModel.saveTask()
		assertThat(repository.jobs.value.any { it.parentTaskId == task.id }).isTrue()
	}
}
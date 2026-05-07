package vadimerenkov.autasker.habits.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Dialog

@Composable
fun HabitEditDialog(
	onDismissRequest: () -> Unit
) {
	var title by remember { mutableStateOf("") }
	Dialog(
		onDismissRequest = onDismissRequest
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			OutlinedTextField(
				value = title,
				onValueChange = {
					title = it
				}
			)
		}
	}
}
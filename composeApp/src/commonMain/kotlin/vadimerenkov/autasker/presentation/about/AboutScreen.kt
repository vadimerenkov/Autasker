package vadimerenkov.autasker.presentation.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vadimerenkov.autasker.presentation.theme.AutaskerTheme
import java.time.Year

@Composable
fun AboutScreen(
	modifier: Modifier = Modifier
) {
	val year = if (Year.now() > Year.of(2026)) "2026–${Year.now()}" else "2026"
	Column(
		verticalArrangement = Arrangement.spacedBy(16.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier
	) {
		Text(
			text = "Autasker v.0.1.0",
			fontSize = 16.sp
		)
		Text(
			text = "Vadim Erenkov © $year",
			fontSize = 16.sp
		)
	}
}

@Composable
@Preview
private fun AboutScreenPreview() {
	AutaskerTheme {
		AboutScreen()
	}
}
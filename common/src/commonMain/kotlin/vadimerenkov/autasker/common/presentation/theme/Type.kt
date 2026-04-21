package vadimerenkov.autasker.common.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

val AppTypography: Typography @Composable get() = Typography(

	bodyMedium = TextStyle(
		color = MaterialTheme.colorScheme.onBackground,
		fontSize = 14.sp
	),
	bodySmall = TextStyle(
		color = MaterialTheme.colorScheme.onBackground,
		fontSize = 12.sp
	),
	displayLarge = TextStyle(
		color = MaterialTheme.colorScheme.onBackground,
		fontSize = 24.sp
	),
	displaySmall = TextStyle(
		color = MaterialTheme.colorScheme.onBackground,
		fontSize = 16.sp
	)
)

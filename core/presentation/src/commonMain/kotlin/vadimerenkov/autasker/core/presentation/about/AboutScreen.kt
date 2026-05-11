package vadimerenkov.autasker.core.presentation.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources.github
import autasker.core.presentation.generated.resources.kofi_symbol
import autasker.core.presentation.generated.resources.source_code
import autasker.core.presentation.generated.resources.support_developer
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.autasker.core.presentation.theme.AutaskerTheme
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
			text = "Autasker v.1.0.1",
			fontSize = 16.sp,
			color = MaterialTheme.colorScheme.onBackground
		)
		Text(
			text = "Vadim Erenkov © $year",
			fontSize = 16.sp,
			color = MaterialTheme.colorScheme.onBackground
		)
		val handler = LocalUriHandler.current
		val sourceCodeLink = "https://github.com/vadimerenkov/Autasker"
		val kofiLink = "https://ko-fi.com/vadimerenkov"
		TextButton(
			onClick = {
				handler.openUri(sourceCodeLink)
			}
		) {
			Icon(
				painter = painterResource(Res.drawable.github),
				contentDescription = null,
				modifier = Modifier
					.size(24.dp)
			)
			Text(
				text = stringResource(Res.string.source_code),
				style = MaterialTheme.typography.bodyLarge,
				textDecoration = TextDecoration.Underline,
				modifier = Modifier
					.padding(horizontal = 8.dp)
			)
			Icon(
				imageVector = Icons.AutoMirrored.Filled.OpenInNew,
				contentDescription = null
			)
		}
		TextButton(
			onClick = {
				handler.openUri(kofiLink)
			}
		) {
			Icon(
				painter = painterResource(Res.drawable.kofi_symbol),
				contentDescription = null,
				tint = Color.Unspecified,
				modifier = Modifier
					.size(24.dp)
			)
			Text(
				text = stringResource(Res.string.support_developer),
				style = MaterialTheme.typography.bodyLarge,
				textDecoration = TextDecoration.Underline,
				modifier = Modifier
					.padding(horizontal = 8.dp)
			)
			Icon(
				imageVector = Icons.AutoMirrored.Filled.OpenInNew,
				contentDescription = null
			)
		}
	}
}

@Composable
@Preview
private fun AboutScreenPreview() {
	AutaskerTheme {
		AboutScreen()
	}
}
package vadimerenkov.autasker.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project

internal fun Project.configureAndroidCompose(
	commonExtension: ApplicationExtension
) {
	with (commonExtension) {
		buildFeatures {
			compose = true
		}
	}
}
plugins {
	alias(libs.plugins.androidLint)
	alias(libs.plugins.composeMultiplatform)
	alias(libs.plugins.composeCompiler)
	alias(libs.plugins.composeHotReload)
	alias(libs.plugins.convention.kmp.library)
}

kotlin {

	sourceSets {
		commonMain {
			dependencies {
				implementation(libs.kotlin.stdlib)
				implementation(libs.compose.runtime)
				implementation(libs.compose.foundation)
				implementation(libs.compose.ui)
				implementation(libs.material3)
				implementation(libs.compose.ui.tooling.preview)
				implementation(libs.compose.components.resources)
				implementation(libs.material3.adaptive)
				implementation(libs.androidx.lifecycle.runtimeCompose)
				implementation(libs.androidx.lifecycle.viewmodelCompose)
				implementation(libs.bundles.koin)


				implementation(projects.core.domain)
				implementation(projects.core.presentation)
			}
		}

		androidMain {
			dependencies {
				// Add Android-specific dependencies here. Note that this source set depends on
				// commonMain by default and will correctly pull the Android artifacts of any KMP
				// dependencies declared in commonMain.
			}
		}
	}
}

dependencies {
	androidRuntimeClasspath(libs.compose.ui.tooling)
}
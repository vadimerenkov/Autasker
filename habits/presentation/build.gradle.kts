plugins {
	alias(libs.plugins.convention.kmp.library)
	alias(libs.plugins.androidLint)
	alias(libs.plugins.composeMultiplatform)
	alias(libs.plugins.composeCompiler)
	alias(libs.plugins.composeHotReload)
}

kotlin {


	// Source set declarations.
	// Declaring a target automatically creates a source set with the same name. By default, the
	// Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
	// common to share sources between related targets.
	// See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
	sourceSets {
		commonMain {
			dependencies {
				implementation(libs.kotlin.stdlib)
				implementation(libs.compose.runtime)
				implementation(libs.compose.foundation)
				implementation(libs.compose.ui)
				implementation(libs.material3)
				implementation(libs.material.icons.extended)
				implementation(libs.compose.components.resources)
				implementation(libs.compose.ui.tooling.preview)
				implementation(libs.androidx.lifecycle.viewmodelCompose)
				implementation(libs.androidx.lifecycle.runtimeCompose)
				implementation(libs.calendar)
				implementation(libs.bundles.koin)

				implementation(projects.core.domain)
				implementation(projects.habits.domain)
				implementation(projects.core.presentation)
			}
		}

		commonTest {
			dependencies {
				implementation(libs.kotlin.test)
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
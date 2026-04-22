plugins {
	alias(libs.plugins.androidLint)
	alias(libs.plugins.composeMultiplatform)
	alias(libs.plugins.composeCompiler)
	alias(libs.plugins.composeHotReload)
	alias(libs.plugins.convention.kmp.library)
}

kotlin {

	jvm()

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
				implementation(libs.compose.ui.tooling.preview)
				implementation(libs.calendar)
				implementation(libs.bundles.koin)
				implementation(libs.compose.components.resources)
				implementation(libs.material3.adaptive)

				implementation(projects.common)
			}
		}
	}
}

dependencies {
	androidRuntimeClasspath(libs.compose.ui.tooling)
}
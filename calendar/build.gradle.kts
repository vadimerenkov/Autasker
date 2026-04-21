plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.androidKmpLibrary)
	alias(libs.plugins.androidLint)
	alias(libs.plugins.composeMultiplatform)
	alias(libs.plugins.composeCompiler)
	alias(libs.plugins.composeHotReload)

}

kotlin {

	// Target declarations - add or remove as needed below. These define
	// which platforms this KMP module supports.
	// See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
	android {
		namespace = "vadimerenkov.calendar"
		compileSdk {
			version = release(36) {
				minorApiLevel = 1
			}
		}
		minSdk = 26

		withHostTestBuilder {
		}

		withDeviceTestBuilder {
			sourceSetTreeName = "test"
		}.configure {
			instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		}
	}

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

		getByName("androidDeviceTest") {
			dependencies {
				implementation(libs.androidx.runner)
				implementation(libs.androidx.core)
				implementation(libs.androidx.testExt.junit)
			}
		}
	}
}

dependencies {
	androidRuntimeClasspath(libs.compose.ui.tooling)
}
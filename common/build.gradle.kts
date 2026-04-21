plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.androidKmpLibrary)
	alias(libs.plugins.androidLint)
	alias(libs.plugins.room)
	alias(libs.plugins.composeMultiplatform)
	alias(libs.plugins.composeCompiler)
	alias(libs.plugins.composeHotReload)
	alias(libs.plugins.ksp)
	alias(libs.plugins.serialization)
}

kotlin {

	// Target declarations - add or remove as needed below. These define
	// which platforms this KMP module supports.
	// See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
	android {
		namespace = "vadimerenkov.autasker.common"
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

	// For iOS targets, this is also where you should
	// configure native binary output. For more information, see:
	// https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

	// A step-by-step guide on how to include this library in an XCode
	// project can be found here:
	// https://developer.android.com/kotlin/multiplatform/migrate
	jvm()
	jvmToolchain(21)
	// Source set declarations.
	// Declaring a target automatically creates a source set with the same name. By default, the
	// Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
	// common to share sources between related targets.
	// See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
	sourceSets {
		commonMain {
			dependencies {
				implementation(libs.kotlin.stdlib)
				implementation(libs.androidx.room.runtime)
				implementation(libs.androidx.room.compiler)
				implementation(libs.compose.runtime)
				implementation(libs.compose.foundation)
				implementation(libs.compose.ui)
				implementation(libs.material3)
				implementation(libs.compose.components.resources)
				implementation(libs.compose.ui.tooling.preview)
				implementation(libs.androidx.lifecycle.viewmodelCompose)
				implementation(libs.androidx.lifecycle.runtimeCompose)
				implementation(libs.bundles.koin)
				implementation(libs.androidx.datastore)
				implementation(libs.androidx.datastore.preferences)
				implementation(libs.material.icons.extended)
				implementation(libs.reorderable)
				implementation(libs.androidx.sqlite.bundled)
				implementation(libs.appdirs)
			}
		}
		jvmMain.dependencies {
			implementation(libs.quartz)
			implementation(libs.autolaunch)
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

room {
	schemaDirectory("$projectDir/schemas")
}

compose.resources {
	publicResClass = true
}

dependencies {
	ksp(libs.androidx.room.compiler)
	androidRuntimeClasspath(libs.compose.ui.tooling)
}
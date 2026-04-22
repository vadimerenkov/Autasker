plugins {
	alias(libs.plugins.convention.kmp.library)
	alias(libs.plugins.androidLint)
	alias(libs.plugins.room)
	alias(libs.plugins.composeMultiplatform)
	alias(libs.plugins.composeCompiler)
	alias(libs.plugins.composeHotReload)
	alias(libs.plugins.ksp)
}

kotlin {

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
//				implementation(libs.androidx.room.compiler)
				implementation(libs.compose.runtime)
				implementation(libs.compose.foundation)
				implementation(libs.compose.ui)
				implementation(libs.compose.components.resources)
				implementation(libs.material3)
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
		commonTest.dependencies {
			implementation(libs.assertK)
			implementation(libs.junit)
			implementation(libs.kotlinx.coroutines.core)
			implementation(libs.kotlinx.coroutines.test)
			implementation(libs.koin.test)
			implementation(libs.koin.test.junit)
			implementation(project.dependencies.platform("org.junit:junit-bom:6.0.3"))
			implementation(libs.junit.jupiter)
			implementation(libs.junit.platform.launcher)

		}
		jvmMain.dependencies {
			implementation(libs.quartz)
			implementation(libs.autolaunch)
			implementation(compose.desktop.currentOs)
			implementation(libs.kotlinx.coroutinesSwing)
		}

		androidMain {
			dependencies {
				implementation(libs.androidx.activity.compose)
				implementation(libs.koin.android)
				implementation(libs.exoplayer)
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
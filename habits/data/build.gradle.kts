plugins {
	alias(libs.plugins.convention.kmp.library)
	alias(libs.plugins.androidLint)
	alias(libs.plugins.room)
	alias(libs.plugins.ksp)
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
				implementation(libs.androidx.room.runtime)
				implementation(libs.androidx.sqlite.bundled)
				implementation(libs.appdirs)
				implementation(libs.koin.core)

				implementation(projects.habits.domain)
				implementation(projects.core.domain)
				implementation(projects.core.database)
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

dependencies {
	ksp(libs.androidx.room.compiler)
}

room {
	schemaDirectory("$projectDir/schemas")
}
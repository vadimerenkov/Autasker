plugins {
	alias(libs.plugins.convention.kmp.library)
	alias(libs.plugins.androidLint)
	alias(libs.plugins.room)
	alias(libs.plugins.ksp)

}

kotlin {
	sourceSets {
		commonMain {
			dependencies {
				implementation(libs.kotlin.stdlib)
				implementation(libs.androidx.room.runtime)
				implementation(libs.androidx.sqlite.bundled)
				implementation(libs.appdirs)

				implementation(projects.core.domain)
			}
		}

		androidMain {
			dependencies {
				// Add Android-specific dependencies here. Note that this source set depends on
				// commonMain by default and will correctly pull the Android artifacts of any KMP
				// dependencies declared in commonMain.
			}
		}

		jvmMain.dependencies {

		}
	}
}

room {
	schemaDirectory("$projectDir/schemas")
}

dependencies {
	ksp(libs.androidx.room.compiler)
}


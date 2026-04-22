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
				implementation(libs.koin.core)

				implementation(projects.core.domain)
			}
		}

		androidMain {
			dependencies {
				implementation(libs.koin.android)
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


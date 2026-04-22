plugins {
	alias(libs.plugins.convention.kmp.library)
	alias(libs.plugins.androidLint)
	alias(libs.plugins.composeMultiplatform)
	alias(libs.plugins.composeCompiler)
	alias(libs.plugins.composeHotReload)
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
				implementation(libs.material.icons.extended)
				implementation(libs.compose.components.resources)
				implementation(libs.compose.ui.tooling.preview)
				implementation(libs.androidx.lifecycle.viewmodelCompose)
				implementation(libs.androidx.lifecycle.runtimeCompose)
				implementation(libs.androidx.datastore)
				implementation(libs.androidx.datastore.preferences)
				implementation(libs.bundles.koin)
				implementation(libs.reorderable)

				implementation(projects.core.domain)
			}
		}

		androidMain {
			dependencies {
				implementation(libs.koin.android)
			}
		}
	}
}

compose.resources {
	publicResClass = true
}
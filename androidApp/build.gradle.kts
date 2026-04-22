plugins {
	alias(libs.plugins.convention.android.application)
	alias(libs.plugins.composeCompiler)
}

dependencies {
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(libs.koin.android)
	implementation(libs.bundles.koin)
	implementation(libs.androidx.appcompat)
	implementation(libs.material)
	implementation(libs.compose.runtime)
	implementation(libs.compose.foundation)
	implementation(libs.compose.components.resources)
	implementation(libs.material3)
	implementation(libs.material.icons.extended)
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.testExt.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	debugImplementation(libs.compose.ui.tooling.preview)
	debugImplementation(libs.compose.ui.tooling)

	implementation(projects.composeApp)
//	implementation(projects.common)
}
plugins {
	alias(libs.plugins.androidApplication)
	alias(libs.plugins.composeCompiler)
}

android {
	namespace = "vadimerenkov.autasker"
	compileSdk = libs.versions.android.compileSdk.get().toInt()

	defaultConfig {
		applicationId = "vadimerenkov.autasker"
		minSdk = libs.versions.android.minSdk.get().toInt()
		targetSdk = libs.versions.android.targetSdk.get().toInt()
		versionCode = libs.versions.version.code.get().toInt()
		versionName = libs.versions.version.name.get()
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
	buildTypes {
		getByName("release") {
			isMinifyEnabled = true
			isShrinkResources = true
			signingConfig = signingConfigs.getByName("debug")
		}
	}
	buildFeatures {
		compose = true
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
}

dependencies {
	implementation(projects.composeApp)
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(libs.koin.android)
	implementation(libs.bundles.koin)
	implementation(libs.androidx.appcompat)
	implementation(libs.material)
	implementation(libs.compose.runtime)
	implementation(libs.compose.foundation)
	implementation(libs.material3)
	implementation(libs.material.icons.extended)
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.testExt.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	debugImplementation(libs.compose.ui.tooling.preview)
	debugImplementation(libs.compose.ui.tooling)
}
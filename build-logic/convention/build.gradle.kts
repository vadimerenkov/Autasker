import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	`kotlin-dsl`
}

group = "vadimerenkov.autasker.convention.buildlogic"

dependencies {
	compileOnly(libs.android.gradlePlugin)
	compileOnly(libs.android.tools.common)
	compileOnly(libs.kotlin.gradlePlugin)
	compileOnly(libs.compose.gradlePlugin)
}


java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
	compilerOptions {
		jvmTarget = JvmTarget.JVM_17
	}
}

tasks {
	validatePlugins {
		enableStricterValidation = true
		failOnWarning = true
	}
}

gradlePlugin {
	plugins {
		register("androidApplication") {
			id = "vadimerenkov.autasker.convention.android.application"
			implementationClass = "AndroidApplicationConventionPlugin"
		}
		register("kmpLibrary") {
			id = "vadimerenkov.autasker.convention.kmp.library"
			implementationClass = "KmpLibraryConventionPlugin"
		}
	}
}
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
	alias(libs.plugins.convention.kmp.library)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
	        implementation(libs.koin.android)
			implementation(libs.exoplayer)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
	        implementation(libs.compose.ui)
	        implementation(libs.material3)
	        implementation(libs.material.icons.extended)
	        implementation(libs.bundles.koin)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.ui.tooling.preview)
	        implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
	        implementation(libs.kotlinx.serialization.json)
	        implementation(libs.kotlinx.serialization.core)
	        implementation(libs.androidx.datastore)
	        implementation(libs.androidx.datastore.preferences)
	        implementation(libs.bundles.navigation3)
	        implementation(libs.material3.adaptive)

	        implementation(projects.calendar)
	        implementation(projects.core.database)
	        implementation(projects.core.domain)
	        implementation(projects.core.presentation)
	        implementation(projects.habits.domain)
	        implementation(projects.habits.data)
	        implementation(projects.habits.presentation)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
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
    }
}

dependencies {
	androidRuntimeClasspath(libs.compose.ui.tooling)
}

compose.desktop {
    application {
        mainClass = "vadimerenkov.autasker.MainKt"
        nativeDistributions {
			includeAllModules = true
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Rpm)
            packageName = "Autasker"
            packageVersion = libs.versions.version.name.get()
	        description = "Autasker - to-do list app"
	        copyright = "2026 © Vadim Erenkov"
	        vendor = "Vadim Erenkov"
			licenseFile.set(project.file("LICENSE"))

	        windows {
				dirChooser = true
		        menuGroup = "Autasker"
		        iconFile.set(project.file("app_icon.ico"))
	        }

	        linux {
				iconFile.set(project.file("app_icon.png"))
	        }
        }
	    buildTypes.release.proguard {
			obfuscate = true
			configurationFiles.from(project.file("desktop-proguard.pro"))
	    }
    }
}

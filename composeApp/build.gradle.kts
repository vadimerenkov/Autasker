import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
	alias(libs.plugins.room)
	alias(libs.plugins.ksp)
	alias(libs.plugins.convention.kmp.library)
}

kotlin {

	jvmToolchain(21)
    
    jvm()

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
	        implementation(libs.bundles.navigation3)
	        implementation(libs.androidx.datastore)
	        implementation(libs.androidx.datastore.preferences)
	        implementation(libs.material3.adaptive)

	        implementation(projects.common)
	        implementation(projects.calendar)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

room {
	schemaDirectory("$projectDir/schemas")
}

dependencies {
	ksp(libs.androidx.room.compiler)
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

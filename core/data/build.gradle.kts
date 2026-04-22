plugins {
	alias(libs.plugins.convention.kmp.library)
	alias(libs.plugins.androidLint)
}

kotlin {
	sourceSets {
		commonMain {
			dependencies {
				implementation(libs.kotlin.stdlib)
				// Add KMP dependencies here
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
import com.codingfeline.buildkonfig.compiler.FieldSpec

plugins {
	alias(libs.plugins.convention.kmp.library)
	alias(libs.plugins.androidLint)
	alias(libs.plugins.buildkonfig)
}

kotlin {
	sourceSets {
		commonMain {
			dependencies {
				implementation(libs.kotlin.stdlib)
				implementation(libs.bundles.ktor)

				implementation(projects.core.domain)
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

buildkonfig {
	packageName = "vadimerenkov.autasker"

	defaultConfigs {
		buildConfigField(FieldSpec.Type.STRING, "versionName", libs.versions.version.name.get())
	}
}
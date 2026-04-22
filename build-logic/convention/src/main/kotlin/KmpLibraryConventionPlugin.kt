import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import vadimerenkov.autasker.convention.getVersion
import vadimerenkov.autasker.convention.libs
import vadimerenkov.autasker.convention.pathToPackageName

class KmpLibraryConventionPlugin: Plugin<Project> {

	override fun apply(target: Project) {
		with(target) {
			with(pluginManager) {
				apply("com.android.kotlin.multiplatform.library")
				apply("org.jetbrains.kotlin.multiplatform")
				apply("org.jetbrains.kotlin.plugin.serialization")
			}

			extensions.configure<KotlinMultiplatformExtension>() {
				extensions.configure<KotlinMultiplatformAndroidLibraryExtension> {
					compileSdk = libs.getVersion("android-compileSdk").toInt()
					minSdk = libs.getVersion("android-minSdk").toInt()
					namespace = pathToPackageName()
					experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
				}
				jvm()
				jvmToolchain(21)
			}
		}
	}

}
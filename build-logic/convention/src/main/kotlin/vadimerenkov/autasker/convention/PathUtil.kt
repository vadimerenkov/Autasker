package vadimerenkov.autasker.convention

import org.gradle.api.Project

fun Project.pathToPackageName(): String {
	val relativePackageName = path
		.replace(':', '.')
		.lowercase()

	return "vadimerenkov.autasker$relativePackageName"
}
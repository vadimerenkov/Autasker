package vadimerenkov.autasker.update_checker

import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import vadimerenkov.autasker.BuildKonfig

object UpdateChecker {

	suspend fun checkForUpdates(): Boolean {
		val httpClient = HttpClientFactory.create(OkHttp.create())

		return try {
			val response = httpClient.get(
				urlString = "https://api.github.com/repos/vadimerenkov/Autasker/releases/latest"
			)
			when (response.status.value) {
				in 200..299 -> {
					val body = response.body<VersionDto>()
					println("Version fetched is $body")
					compareVersions(body)
				}
				else -> false
			}
		} catch (e: Exception) {
			currentCoroutineContext().ensureActive()
			false
		}
	}

	private fun compareVersions(version: VersionDto): Boolean {
		val currentVersion = BuildKonfig.versionName
		val currentVersions = currentVersion
			.split('.')
			.map { it.toInt() }

		val githubVersion = version.tag_name
		val githubVersions = githubVersion
			.split('.')
			.map { it.toInt() }

		return when {
			githubVersions[0] > currentVersions[0] -> true
			githubVersions[0] == currentVersions[0] && githubVersions[1] > currentVersions[1] -> true
			githubVersions[0] == currentVersions[0] && githubVersions[1] == currentVersions[1] && githubVersions[2] > currentVersions[2] -> true
			else -> false
		}
	}
}
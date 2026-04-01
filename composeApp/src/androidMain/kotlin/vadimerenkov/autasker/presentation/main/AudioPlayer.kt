package vadimerenkov.autasker.presentation.main

import android.content.Context
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import autasker.composeapp.generated.resources.Res

actual class AudioPlayer(
	context: Context
) {
	val player = ExoPlayer.Builder(context).build()

	actual fun play(path: String) {
		val uri = Res.getUri(path).toUri()
		val mediaItem = MediaItem.fromUri(uri)
		player.clearMediaItems()
		player.addMediaItem(mediaItem)
		player.prepare()
		player.seekTo(0)
		player.play()
	}
}
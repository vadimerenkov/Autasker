package vadimerenkov.autasker.core.presentation.main

import android.content.Context
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import autasker.common.generated.resources.Res

class ExoPlayer(
	context: Context
): AudioPlayer {
	val player = ExoPlayer.Builder(context).build()

	override fun play(path: String) {
		val uri = Res.getUri(path).toUri()
		val mediaItem = MediaItem.fromUri(uri)
		player.clearMediaItems()
		player.addMediaItem(mediaItem)
		player.prepare()
		player.seekTo(0)
		player.play()
	}
}
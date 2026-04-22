package vadimerenkov.autasker.core.presentation.main

import autasker.core.presentation.generated.resources.Res
import java.io.File
import java.net.URI
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.FloatControl

class ClipPlayer: AudioPlayer {

	override fun play(path: String) {
		val clip = AudioSystem.getClip()
		val uri = URI(Res.getUri(path))
		try {
			val inputStream = AudioSystem.getAudioInputStream(File(uri))
			clip.open(inputStream)
			val r = clip.getControl(FloatControl.Type.MASTER_GAIN) as FloatControl
			r.value -= 6
			clip.start()
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}
}
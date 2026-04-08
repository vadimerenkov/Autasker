package vadimerenkov.autasker.presentation.main

import autasker.composeapp.generated.resources.Res
import java.io.File
import java.net.URI
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.FloatControl

class ClipPlayer: AudioPlayer {

	override fun play(path: String) {
		val clip = AudioSystem.getClip()
		val uri = URI(Res.getUri(path))
		val inputStream = AudioSystem.getAudioInputStream(File(uri))
		clip.open(inputStream)
		val r = clip.getControl(FloatControl.Type.MASTER_GAIN) as FloatControl
		r.value -= 6
		clip.start()
	}
}
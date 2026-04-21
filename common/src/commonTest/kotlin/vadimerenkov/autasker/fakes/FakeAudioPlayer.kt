package vadimerenkov.autasker.fakes

import vadimerenkov.autasker.common.presentation.main.AudioPlayer


class FakeAudioPlayer: AudioPlayer {

	override fun play(path: String) {
		println("Played audio at path $path")
	}
}
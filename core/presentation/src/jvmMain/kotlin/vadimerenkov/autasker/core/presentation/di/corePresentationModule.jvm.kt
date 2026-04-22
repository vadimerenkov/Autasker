package vadimerenkov.autasker.core.presentation.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import vadimerenkov.autasker.core.presentation.main.AudioPlayer
import vadimerenkov.autasker.core.presentation.main.ClipPlayer

actual val platformCorePresentationModule = module {
	singleOf(::ClipPlayer).bind<AudioPlayer>()
}
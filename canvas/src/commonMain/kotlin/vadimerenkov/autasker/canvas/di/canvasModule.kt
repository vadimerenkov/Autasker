package vadimerenkov.autasker.canvas.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import vadimerenkov.autasker.canvas.CanvasViewModel

val canvasModule = module {
	viewModelOf(::CanvasViewModel)
}
package vadimerenkov.autasker.presentation.bin

sealed interface BinAction {
	data object Clear: BinAction
	data class DeleteColumnForever(val id: Long): BinAction
	data class RestoreColumn(val id: Long): BinAction
}
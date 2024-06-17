package com.example.doit.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doit.data.DoneEntity
import com.example.doit.data.NoteEntity
import com.example.doit.domain.DoneRepository
import com.example.doit.domain.NoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

//public fun <T> Flow<T>.mutableStateIn(
//    scope: CoroutineScope,
//    initialValue: T
//): MutableStateFlow<T> {
//    val flow = MutableStateFlow(initialValue)
//
//    scope.launch {
//        this@mutableStateIn.collect(flow)
//    }
//
//    return flow
//}

data class NoteValue(
    val data:List<NoteEntity>,
    val state:List<Boolean>
)
class NotesViewModel(private val noteRepository: NoteRepository, private val doneRepository: DoneRepository):ViewModel() {
      val notes = noteRepository
         .getAllNotes()
//         .mutableStateIn(
//             initialValue = listOf(),
//             scope = viewModelScope
//         )
         .stateIn(
             scope = viewModelScope,
             started = SharingStarted.WhileSubscribed(5000L),
             initialValue = listOf()
         )
      val dones = doneRepository
          .getAllNotes()
          .stateIn(
              scope = viewModelScope,
              started = SharingStarted.WhileSubscribed(5000L),
              initialValue = listOf()
          )

    var takeInput by mutableStateOf(false)
    var deleteNote by mutableStateOf(false)
    var deleteDone by mutableStateOf(false)
    var deleteIndex by mutableIntStateOf(-1) //using it for both update and delete
//    var needsUpdate by mutableStateOf(false)
    var completed by mutableStateOf(false)
    var showSheet by mutableStateOf(false)
    var showCompletes by mutableStateOf(true)
   var showMenu by mutableStateOf(false)
    val snackbarHostState = SnackbarHostState()
    // For Pending Notes
    suspend fun saveNote(noteEntity: NoteEntity){
        noteRepository.insertNote(noteEntity)
    }
    suspend fun removeNote(noteEntity: NoteEntity){
        noteRepository.deleteNote(noteEntity)
    }
    suspend fun updateNote(noteEntity: NoteEntity){
        noteRepository.updateNote(noteEntity)
    }
    //For Completed List
    suspend fun saveDone(doneEntity: DoneEntity){
        doneRepository.insert(doneEntity)
    }
    suspend fun removeDone(doneEntity: DoneEntity){
        doneRepository.delete(doneEntity)
    }
    suspend fun clearTODOS(){
        noteRepository.clear()
    }
    suspend fun clearDones(){
        doneRepository.clear()
    }
}

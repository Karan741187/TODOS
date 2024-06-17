package com.example.doit.data

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.doit.NoteApplication
import com.example.doit.ui.components.NotesViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            NotesViewModel(noteApplication().container.noteRepository,noteApplication().container.doneRepository)
        }

    }
}

fun CreationExtras.noteApplication(): NoteApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as NoteApplication)
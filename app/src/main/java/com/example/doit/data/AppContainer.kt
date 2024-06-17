package com.example.doit.data

import android.content.Context
import com.example.doit.domain.DoneRepository
import com.example.doit.domain.NoteRepository

interface AppContainer{
    val noteRepository: NoteRepository
    val doneRepository: DoneRepository
}

class AppDataContainer(private val context: Context): AppContainer {
    override val noteRepository: NoteRepository by lazy{
        NativeNoteRepository(NoteDatabase.getDatabase(context).noteDao())
    }
    override val doneRepository: DoneRepository by lazy {
        NativeDoneRepository(NoteDatabase.getDatabase(context).doneDao())
    }
}
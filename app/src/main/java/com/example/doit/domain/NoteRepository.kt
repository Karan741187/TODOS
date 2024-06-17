package com.example.doit.domain

import com.example.doit.data.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NoteRepository{
    fun getAllNotes(): Flow<List<NoteEntity>>
    fun getNote(id:Int):Flow<NoteEntity>
    suspend fun updateNote(note: NoteEntity)
    suspend fun insertNote(note: NoteEntity)
    suspend fun deleteNote(note: NoteEntity)
    suspend fun clear()
}
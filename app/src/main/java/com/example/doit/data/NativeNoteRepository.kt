package com.example.doit.data

import com.example.doit.domain.NoteRepository
import kotlinx.coroutines.flow.Flow

class NativeNoteRepository(private val noteDao: NoteDao): NoteRepository {
    override fun getAllNotes(): Flow<List<NoteEntity>> = noteDao.getAllNotes()

    override fun getNote(id: Int): Flow<NoteEntity> = noteDao.getNote(id)

    override suspend fun updateNote(note: NoteEntity) = noteDao.update(note)

    override suspend fun insertNote(note: NoteEntity) = noteDao.insert(note)

    override suspend fun deleteNote(note: NoteEntity) = noteDao.delete(note)

    override suspend fun clear() = noteDao.clear()
}
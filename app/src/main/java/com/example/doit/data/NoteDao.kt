package com.example.doit.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("select * from Notes")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("select * from Notes where id = :id")
    fun getNote(id:Int):Flow<NoteEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(noteEntity: NoteEntity)

    @Update
    suspend fun update(noteEntity: NoteEntity)

    @Delete
    suspend fun delete(noteEntity: NoteEntity)

    @Query("delete from Notes")
    suspend fun clear()
}
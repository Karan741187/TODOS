package com.example.doit.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DoneDao {
    @Query("select * from Completed")
    fun getAllNotes(): Flow<List<DoneEntity>>

    @Delete
    suspend fun delete(doneEntity: DoneEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(doneEntity: DoneEntity)

    @Query("delete from Completed")
    suspend fun clear()
}
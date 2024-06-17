package com.example.doit.domain

import com.example.doit.data.DoneEntity
import kotlinx.coroutines.flow.Flow

interface DoneRepository {
    fun getAllNotes(): Flow<List<DoneEntity>>
    suspend fun delete(doneEntity: DoneEntity)
    suspend fun insert(doneEntity: DoneEntity)
    suspend fun clear()
}
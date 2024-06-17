package com.example.doit.data

import com.example.doit.domain.DoneRepository
import kotlinx.coroutines.flow.Flow

class NativeDoneRepository(private val doneDao: DoneDao): DoneRepository {
    override fun getAllNotes(): Flow<List<DoneEntity>> = doneDao.getAllNotes()

    override suspend fun delete(doneEntity: DoneEntity) = doneDao.delete(doneEntity)
    override suspend fun insert(doneEntity: DoneEntity) = doneDao.insert(doneEntity)
    override suspend fun clear() = doneDao.clear()

}
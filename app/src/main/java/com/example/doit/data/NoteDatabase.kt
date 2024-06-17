package com.example.doit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NoteEntity::class, DoneEntity::class],version=1, exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun doneDao(): DoneDao

    companion object {
        @Volatile
        private var Instance: NoteDatabase?=null

        fun getDatabase(context: Context): NoteDatabase {
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context, NoteDatabase::class.java,"note_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also{ Instance =it}
            }
        }
    }
}
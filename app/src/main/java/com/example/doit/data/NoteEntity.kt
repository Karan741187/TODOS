package com.example.doit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val title:String,
    val description:String,
    val state:Boolean=false
)
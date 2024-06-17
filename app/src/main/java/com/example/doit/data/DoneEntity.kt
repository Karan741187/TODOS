package com.example.doit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Completed")
data class DoneEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val title:String,
    val description:String,
    val state:Boolean = false
)
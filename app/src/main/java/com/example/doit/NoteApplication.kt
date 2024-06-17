package com.example.doit

import android.app.Application
import com.example.doit.data.AppDataContainer

class NoteApplication: Application() {
    lateinit var container: AppDataContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
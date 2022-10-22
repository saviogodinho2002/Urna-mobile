package com.example.urnaeletrnica

import android.app.Application
import com.example.urnaeletrnica.model.dao.AppDataBase

class App:Application() {
    lateinit var db:AppDataBase
    override fun onCreate() {
        super.onCreate()

        db = AppDataBase.getDatabase(this)

    }
}
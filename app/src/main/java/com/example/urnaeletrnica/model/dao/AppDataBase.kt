package com.example.urnaeletrnica.model.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.urnaeletrnica.model.entities.Candidate
import com.example.urnaeletrnica.model.entities.Office
import com.example.urnaeletrnica.model.entities.Party

@Database(entities = [Candidate::class,Party::class,Office::class], version = 3)
//@TypeConverters(DateConverter::class)
abstract class AppDataBase : RoomDatabase() {

   abstract fun OfficeDao():OfficeDao
   abstract fun CandidateDao(): CandidateDao
   abstract fun PartyDao():PartyDao


    companion object {

        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context) : AppDataBase {
            return if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java,
                        "UrnaEletronica"
                    ).build()
                }
                INSTANCE as AppDataBase
            } else {
                INSTANCE as AppDataBase
            }
        }
    }

}
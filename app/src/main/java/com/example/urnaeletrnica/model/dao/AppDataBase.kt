package com.example.urnaeletrnica.model.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.urnaeletrnica.model.entities.*

@Database(entities = [
    Candidate::class,
    Party::class,
    Office::class,
    Plate::class,
    Voter::class,
    Zone::class,
    Section::class,
    VotesElection::class,
                     ], version = 12)
//@TypeConverters(DateConverter::class)
abstract class AppDataBase : RoomDatabase() {

   abstract fun OfficeDao():OfficeDao
   abstract fun CandidateDao(): CandidateDao
   abstract fun PartyDao():PartyDao
   abstract fun PlateDao():PlateDao
   abstract fun VoterDao():VoterDao
   abstract fun ZoneDao():ZoneDao
   abstract fun SectionDao():SectionDao
   abstract fun VotesElectionDao():VotesElectionDao

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
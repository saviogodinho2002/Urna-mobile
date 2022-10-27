package com.example.urnaeletrnica.model.dao

import androidx.room.*
import com.example.urnaeletrnica.model.entities.Plate
import com.example.urnaeletrnica.model.entities.Voter

@Dao
interface VoterDao {
    @Insert
    fun insertVoter(voter: Voter)

    @Query("SELECT * FROM Voter")
    fun getVoterRegisters():List<Voter>;

    @Query("SELECT * FROM Voter Where id = :id")
    fun getVoterByID(id:Int): Voter;

    @Delete
    fun deleteVoter(voter: Voter): Int

    @Update
    fun updateVoter(voter: Voter)
}
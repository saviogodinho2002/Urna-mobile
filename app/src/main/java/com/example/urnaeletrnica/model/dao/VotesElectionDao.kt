package com.example.urnaeletrnica.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.urnaeletrnica.model.entities.VotesElection

@Dao
interface VotesElectionDao {
    @Insert
    fun insertVotesElection(votesElection: VotesElection)

    @Delete
    fun deleteVotesElection(votesElection: VotesElection)

    @Update
    fun updateVotesElection(votesElection: VotesElection)

    @Query("SELECT * from VotesElection")
    fun getVotesElections():List<VotesElection>


    @Query("SELECT * from VotesElection where voter_id = :id")
    fun getVotesElectionByVoterId(id:Int):List<VotesElection>
}
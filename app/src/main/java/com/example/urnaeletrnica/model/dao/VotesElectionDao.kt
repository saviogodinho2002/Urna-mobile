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

    @Query("SELECT * from VotesElection join Voter on VotesElection.voter_id = Voter.id and Voter.sectionId = :sectionId")
    fun getVotesElectionsOfUrn(sectionId:Int):List<VotesElection>


    @Query("SELECT count(*) from VotesElection " +
            "join Candidate on Candidate.numberCandidate = VotesElection.voted_number and Candidate.officeId = :officeId and VotesElection.office_id = :officeId" )
    fun totalValidVotesToOfficeExecutiveMatchCandidate(officeId: Int):Int

    @Query("SELECT count(*) from VotesElection " +
            "join Plate join Candidate on (Candidate.id = Plate.mainId and Candidate.officeId = :officeId) " +
            "join Party on Party.id = Candidate.partyId and Party.number = VotesElection.voted_number" +
            " and VotesElection.office_id = :officeId" )
    fun totalValidVotesToOfficeExecutiveMatchPlate(officeId: Int):Int

    @Query("SELECT count(*) FROM VotesElection")
    fun totalVotes():Int

    @Query("SELECT count(*) from VotesElection " +
            "join Party on Party.number = VotesElection.voted_number  and VotesElection.office_id = :officeId" )
    fun totalValidVotesToOfficeMatchParty(officeId: Int):Int

    @Query("SELECT count(*) from VotesElection " +
            "join Candidate on (Candidate.numberCandidate = VotesElection.voted_number and Candidate.officeId = VotesElection.office_id)" +
            "join Office on Candidate.officeId = Office.id and Office.isExecutive = 1" )
    fun totalValidVotesExecutives():Int


    @Query("delete from VotesElection")
    fun truncateVotesElections()

    @Query("SELECT * from VotesElection where voter_id = :id")
    fun getVotesElectionByVoterId(id:Int):List<VotesElection>
}
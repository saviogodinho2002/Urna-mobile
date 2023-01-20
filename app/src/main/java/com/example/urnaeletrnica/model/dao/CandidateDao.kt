package com.example.urnaeletrnica.model.dao

import androidx.room.*
import com.example.urnaeletrnica.model.entities.Candidate
import com.example.urnaeletrnica.model.entities.Party
import com.example.urnaeletrnica.model.relationship.CandidateDto
import java.util.Optional

@Dao
interface CandidateDao {
    @Insert
    fun insertCandidate(candidate: Candidate)

    @Query("SELECT * FROM Candidate") //variavel dinamica, mesma da função de baixo
    fun getCandidates():List<Candidate> ;

    @Query("SELECT * FROM Candidate Where id = :id") //variavel dinamica, mesma da função de baixo
    fun getCandidateByID(id:Int):Candidate;

    @Query("SELECT * FROM Candidate Where voterID = :id") //variavel dinamica, mesma da função de baixo
    fun getCandidateByVoterId(id:Int):Candidate;

    @Query("SELECT * FROM Candidate Where numberCandidate = :number") //variavel dinamica, mesma da função de baixo
    fun getCandidateByNumber(number:String):Candidate;

    @Query("SELECT Candidate.id as id, Voter.name as voterName, Party.initials as partyInitials, Office.name as officeName, Candidate.numberCandidate as numberCandidate, Party.id as partyId, Office.id as officeId, Voter.photoUri as photoUri"
            +" from Candidate join Voter on Candidate.voterID = Voter.id join Party on Candidate.partyId = Party.id join Office on Candidate.officeId = Office.id")
    fun getCandidatesDto():List<CandidateDto>

    @Query("SELECT Candidate.id as id, Voter.name as voterName, Party.initials as partyInitials, Office.name as officeName, Candidate.numberCandidate as numberCandidate, Party.id as partyId, Office.id as officeId, Voter.photoUri as photoUri"
            +" from Candidate join Voter on Candidate.voterID = Voter.id join Party on Candidate.partyId = Party.id join Office on Candidate.officeId = Office.id and Candidate.voterID = :voterId")
    fun getCandidateDtoByVoterId(voterId:Int):CandidateDto

    @Query("SELECT * FROM Candidate Where officeId = :officeId")
    fun getCandidatesByOffice(officeId: Int):List<Candidate>;

    @Delete
    fun deleteCandidate(candidate: Candidate): Int

    @Update
    fun updateCandidate(candidate: Candidate)

}
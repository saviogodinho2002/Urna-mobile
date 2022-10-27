package com.example.urnaeletrnica.model.dao

import androidx.room.*
import com.example.urnaeletrnica.model.entities.Candidate
import com.example.urnaeletrnica.model.entities.Party

@Dao
interface CandidateDao {
    @Insert
    fun insertCandidate(candidate: Candidate)


    @Query("SELECT * FROM Candidate Where id = :id") //variavel dinamica, mesma da função de baixo
    fun getCandidateByID(id:Int):Candidate;

    @Query("SELECT * FROM Candidate Where numberCandidate = :number") //variavel dinamica, mesma da função de baixo
    fun getCandidateByNumber(number:String):Candidate;



    @Query("SELECT * FROM Candidate Where officeId = :officeId")
    fun getCandidatesByOffice(officeId: Int):List<Candidate>;

    @Delete
    fun deleteCandidate(candidate: Candidate): Int

    @Update
    fun updateCandidate(candidate: Candidate)

}
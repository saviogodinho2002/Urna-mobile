package com.example.urnaeletrnica.controllers

import android.content.ContentResolver
import android.content.Context
import com.example.urnaeletrnica.model.dao.CandidateDao
import com.example.urnaeletrnica.model.dao.OfficeDao
import com.example.urnaeletrnica.model.entities.Candidate

class DataBankCandidateController(private val applicationContext: Context, private val contentResolver: ContentResolver, private val dao: CandidateDao) {
    fun getCandidates():List<Candidate> = dao.getCandidates()

    fun saveCandidate(voterId:Int,officeId:Int,partyId:Int,numberCandidate: String?):Candidate{
        val candidate = Candidate(
            voterID = voterId,
            officeId = officeId,
            partyId= partyId,
            numberCandidate = numberCandidate
        )
        dao.insertCandidate(candidate)
        return candidate
    }
    fun deleteCandidate(candidate: Candidate){
        dao.deleteCandidate(candidate)
    }

}
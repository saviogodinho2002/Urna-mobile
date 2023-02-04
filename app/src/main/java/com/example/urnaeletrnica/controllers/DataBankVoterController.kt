package com.example.urnaeletrnica.controllers

import android.content.ContentResolver
import android.content.Context
import com.example.urnaeletrnica.model.dao.VoterDao
import com.example.urnaeletrnica.model.entities.Voter

class DataBankVoterController(private val applicationContext: Context, private val contentResolver: ContentResolver, private val dao: VoterDao) {
    fun getVoters() = dao.getVoterRegisters()
    fun saveVoter(voterName:String,voterTittle:String, sectionId:Int,photoDirectory:String?):Voter{
        val voter = Voter(
            name = voterName,
            voterTitle = voterTittle,
            photoUri = photoDirectory,
            sectionId = sectionId
        )
        dao.insertVoter(voter)
        return voter
    }
    fun getVoterByTittle(tittle:String) = dao.getVoterByTittle(tittle)
    fun deleteVoter(voter: Voter){
        dao.deleteVoter(voter)
    }
    fun getVoterByCandidateId(id:Int) = dao.getVoterByCandidateId(id);
}
package com.example.urnaeletrnica.controllers

import android.content.ContentResolver
import android.content.Context
import com.example.urnaeletrnica.model.dao.VotesElectionDao
import com.example.urnaeletrnica.model.entities.VotesElection

class DataBankVotesElectionController(private val applicationContext: Context, private val contentResolver: ContentResolver, private val dao: VotesElectionDao) {
    fun getVotesElectionByVoterId(id:Int) = dao.getVotesElectionByVoterId(id)

    fun saveVoteElection(votesElection: VotesElection){
        dao.insertVotesElection(votesElection)
    }
    fun truncateVotesElections() = dao.truncateVotesElections()
    fun getVotesSections() = dao.getVotesElections()
    fun getVotesElectionsOfUrn(sectionId:Int) = dao.getVotesElectionsOfUrn(sectionId)
    fun totalValidVotesToOfficeNotExecutive(officeId: Int):Int {

        return dao.totalValidVotesToOfficeExecutiveMatchCandidate(officeId) //+ dao.totalValidVotesToOfficeMatchParty(officeId)
    }
    fun totalValidVotesToOfficeExecutive(officeId: Int):Int {

        return dao.totalValidVotesToOfficeExecutiveMatchPlate(officeId) //+ dao.totalValidVotesToOfficeMatchParty(officeId)
    }

    fun totalVotes() = dao.totalVotes()
}
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
    fun totalValidVotesToOfficeExecutiveMatchCandidateOnSection(officeId: Int,sectionId: Int) = dao.totalValidVotesToOfficeExecutiveMatchCandidateOnSection(officeId,sectionId)
    fun totalValidVotesToOfficeExecutive(officeId: Int):Int {

        return dao.totalValidVotesToOfficeExecutiveMatchPlate(officeId) //+ dao.totalValidVotesToOfficeMatchParty(officeId)
    }
    fun totalValidVotesToOfficeExecutiveOnSection(officeId: Int,sectionId: Int):Int {

        return dao.totalValidVotesToOfficeExecutiveMatchPlateOnSection(officeId,sectionId) //+ dao.totalValidVotesToOfficeMatchParty(officeId)
    }

    fun totalVotes() = dao.totalVotes()

    fun getVotesSectionsToNumber(number:String,officeId: Int) = dao.getVotesSectionsToNumber(number,officeId)
    fun getVotesSectionsToNumberOnSection(number:String,officeId: Int,sectionId: Int) = dao.getVotesSectionsToNumberOnSection(number,officeId,sectionId)

    fun getVotesSectionsToNumberExecutive(number:String,officeId: Int) = dao.getVotesSectionsToNumberExecutive(number,officeId)

    fun getVotesSectionsToNumberExecutiveOnSection(number:String,officeId: Int,sectionId: Int) = dao.getVotesSectionsToNumberExecutiveOnSection(number,officeId,sectionId)
}
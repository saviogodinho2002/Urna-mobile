package com.example.urnaeletrnica.controllers

import android.content.ContentResolver
import android.content.Context
import com.example.urnaeletrnica.model.dao.VotesElectionDao

class DataBankVotesElectionController(private val applicationContext: Context, private val contentResolver: ContentResolver, private val dao: VotesElectionDao) {
    fun getVotesElectionByVoterId(id:Int) = dao.getVotesElectionByVoterId(id)
}
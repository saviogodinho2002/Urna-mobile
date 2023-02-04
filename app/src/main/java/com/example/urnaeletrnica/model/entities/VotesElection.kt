package com.example.urnaeletrnica.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VotesElection (
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    @ColumnInfo(name = "voter_id") val voterId:Int,
    @ColumnInfo(name = "office_id") val officeId:Int,
    @ColumnInfo(name = "voted_number") val votedNumber:String,

)
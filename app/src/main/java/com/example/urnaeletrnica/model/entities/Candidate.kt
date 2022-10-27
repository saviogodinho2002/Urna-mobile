package com.example.urnaeletrnica.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Candidate(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    @ColumnInfo(name = "voterID") val voterID:Int, //chaveestrangeira
    @ColumnInfo(name = "officeId") val officeId:Int,
    @ColumnInfo(name = "numberCandidate") val numberCandidate: String?,

)

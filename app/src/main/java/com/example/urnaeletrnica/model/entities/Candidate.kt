package com.example.urnaeletrnica.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Candidate(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    @ColumnInfo(name = "voterID") val voterID:Int, //chaveestrangeira
    @ColumnInfo(name = "photoUri") val photoUri:String?,
    @ColumnInfo(name = "partyId") val partyId:Int, // chave estrangeira
    @ColumnInfo(name = "number") val number: String,
    @ColumnInfo(name = "timeStamp") val timestamp: Long

)

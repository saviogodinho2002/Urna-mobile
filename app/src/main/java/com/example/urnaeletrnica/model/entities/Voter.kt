package com.example.urnaeletrnica.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Voter(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    @ColumnInfo(name = "name") val name:String,
    @ColumnInfo(name = "photoUri") val photoUri: String?,
    @ColumnInfo(name = "voterTitle") val voterTitle:String,
    @ColumnInfo(name = "partyAffiliateId") val partyAffiliate:Int?
)

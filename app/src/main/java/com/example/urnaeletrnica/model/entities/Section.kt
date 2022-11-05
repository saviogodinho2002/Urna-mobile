package com.example.urnaeletrnica.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Section(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    @ColumnInfo (name = "zoneId") val zoneId:Int,
    @ColumnInfo(name = "sectionNumber") val sectionNumber:String
)

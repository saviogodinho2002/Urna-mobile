package com.example.urnaeletrnica.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Zone(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    @ColumnInfo(name = "sectionNumber") val sectionNumber:String,
    @ColumnInfo(name = "sectionName") val sectionName:String
)

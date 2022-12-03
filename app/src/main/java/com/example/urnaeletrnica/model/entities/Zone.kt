package com.example.urnaeletrnica.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Zone(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    @ColumnInfo(name = "zoneNumber") val zoneNumber:String,
    @ColumnInfo(name = "zoneName") val zoneName:String
)

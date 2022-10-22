package com.example.urnaeletrnica.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity
data class Office(
    @PrimaryKey(autoGenerate = true) val id:Int,
    @ColumnInfo(name = "name") val name:String,
    @ColumnInfo(name = "numberQuant") val numberQuant:Int,
    @ColumnInfo(name = "timeStamp") val timestamp: Long
)

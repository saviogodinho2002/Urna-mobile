package com.example.urnaeletrnica.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Party(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    @ColumnInfo(name = "initials") val initials:String,
    @ColumnInfo(name = "logoPhoto") val logoPhoto:String?,
    @ColumnInfo(name = "name") val name:String,
    @ColumnInfo(name = "number") val number:String,
    @ColumnInfo(name = "timeStamp") val timeStamp: Long
)

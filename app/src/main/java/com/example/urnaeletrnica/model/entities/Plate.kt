package com.example.urnaeletrnica.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Plate(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    @ColumnInfo(name = "mainId") val mainId:Int,
    @ColumnInfo(name = "viceId") val viceId:Int
)

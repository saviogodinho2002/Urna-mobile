package com.example.urnaeletrnica.model.relationship

import com.example.urnaeletrnica.model.entities.Party
import com.example.urnaeletrnica.model.entities.Plate


data class PlateDto(
    val partyName:String,
    val partyInitials:String,
    val partyNumber:String,
    val partyPhotoUrl:String,
    val plateName:String,


    )

package com.example.urnaeletrnica.model.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.example.urnaeletrnica.model.entities.Section
import com.example.urnaeletrnica.model.entities.Zone

data class SectionAndZone(

    @Embedded  val zone: Zone ,
    @Relation(
        parentColumn = "id",
        entityColumn = "zoneId"
    )
    val section: Section

)

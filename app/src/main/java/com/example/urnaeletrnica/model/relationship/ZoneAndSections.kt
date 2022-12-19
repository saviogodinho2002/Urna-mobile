package com.example.urnaeletrnica.model.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.example.urnaeletrnica.model.entities.Section
import com.example.urnaeletrnica.model.entities.Zone
data class ZoneAndSections(

    @Embedded  val zone: Zone? ,
    @Relation(
        parentColumn = "id",
        entityColumn = "zoneId"
    )
    val section: List<Section>?

){
    fun getSectionAndZone():List<SectionAndZone>{
        val list = mutableListOf<SectionAndZone>()
        section?.forEach{
            list.add(SectionAndZone(zone,it))
        }
        return list
    }
}

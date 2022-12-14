package com.example.urnaeletrnica.model.relationship

import androidx.room.Relation
import com.example.urnaeletrnica.model.entities.Section
import com.example.urnaeletrnica.model.entities.Zone

data class SectionAndZone(
    val zone: Zone?,
    val section: Section?)

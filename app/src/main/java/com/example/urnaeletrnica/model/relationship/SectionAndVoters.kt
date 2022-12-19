package com.example.urnaeletrnica.model.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.example.urnaeletrnica.model.entities.Section
import com.example.urnaeletrnica.model.entities.Voter


data class SectionAndVoters(
    @Embedded val section: Section?,
    @Relation(
        parentColumn = "id",
        entityColumn = "sectionId"
    )
    val voters: List<Voter>?
){
    fun getVoterAndSection(): List<VoterAndSection>{
        val list = mutableListOf<VoterAndSection>()
        voters?.forEach {voter->
                list.add(VoterAndSection(voter,section))
            }
        return list
    }
}

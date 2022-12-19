package com.example.urnaeletrnica.model.relationship

import com.example.urnaeletrnica.model.entities.Section
import com.example.urnaeletrnica.model.entities.Voter
import com.example.urnaeletrnica.model.entities.Zone

data class ZoneSectionVoter(val zone: Zone,val section: Section,val voter: Voter)

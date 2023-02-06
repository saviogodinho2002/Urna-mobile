package com.example.urnaeletrnica.model.relationship

import com.example.urnaeletrnica.model.entities.Candidate

data class CandidateDto(val id:Int, val voterName:String, val voterTittle:String, val isExecutive:Boolean,
                        val partyInitials:String,val officeName:String, val numberCandidate: String,
                        val partyId:Int,val officeId:Int, val photoUri:String?)

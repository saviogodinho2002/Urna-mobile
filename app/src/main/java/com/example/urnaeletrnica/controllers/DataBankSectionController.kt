package com.example.urnaeletrnica.controllers

import android.content.ContentResolver
import android.content.Context
import com.example.urnaeletrnica.model.dao.SectionDao
import com.example.urnaeletrnica.model.entities.Section

class DataBankSectionController (private val applicationContext: Context, private val contentResolver: ContentResolver, private val daoSection: SectionDao) {
    fun getSections():List<Section>{
        return daoSection.getSections();
    }
    fun deleteSection(section: Section){
        daoSection.deleteSection(section);
    }
    fun getSectionAndVoters() = daoSection.getVotersAndSections()

    fun getSectionById(id: Int):Section = daoSection.getSectionById(id)

    fun saveSection(sectionNum:String, zoneId: Int): Section {
        val section = Section( sectionNumber = sectionNum, zoneId = zoneId)
        daoSection.insertSection(section)
        return section
    }
}
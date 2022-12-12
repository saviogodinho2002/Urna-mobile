package com.example.urnaeletrnica.controllers

import android.content.ContentResolver
import android.content.Context
import com.example.urnaeletrnica.model.dao.OfficeDao
import com.example.urnaeletrnica.model.dao.SectionDao
import com.example.urnaeletrnica.model.entities.Office
import com.example.urnaeletrnica.model.entities.Section

class DataBankSectionController (private val applicationContext: Context, private val contentResolver: ContentResolver, private val dao: SectionDao) {
    fun getSections():List<Section>{
        return dao.getSections();
    }
    fun deleteOffice(section: Section){
        dao.deleteSection(section);
    }
    fun saveOffice(nameOffice:String,number:Int, executive:Boolean): Section? {

        return null
    }
}
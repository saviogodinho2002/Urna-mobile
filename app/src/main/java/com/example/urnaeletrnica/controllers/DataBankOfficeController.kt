package com.example.urnaeletrnica.controllers

import android.content.ContentResolver
import android.content.Context
import com.example.urnaeletrnica.model.dao.OfficeDao
import com.example.urnaeletrnica.model.dao.PartyDao
import com.example.urnaeletrnica.model.entities.Office

class DataBankOfficeController (private val applicationContext: Context, private val contentResolver: ContentResolver, private val dao: OfficeDao) {
   fun getOffices():List<Office>{
       return dao.getOffices();
   }
    fun deleteOffice(office: Office){
        dao.deleteOffice(office);
    }
    fun saveOffice(nameOffice:String,number:Int, executive:Boolean):Office{
        val office = Office(
            name = nameOffice,
            numberLength = number,
            isExecutive = executive
        )
        dao.insertOffice(
            office
        )
        return office;
    }
    fun getOfficesExecutive() = dao.getOfficesExecutive()
    fun getOfficesIsNotExecutive() = dao.getOfficesIsNotExecutive()

    fun getOfficesIsNotExecutiveHasCandidate() = dao.getOfficesIsNotExecutiveHasCandidate()
    fun getOfficesExecutiveHasPlate() = dao.getOfficesExecutiveHasPlate()


}
package com.example.urnaeletrnica.controllers

import android.content.ContentResolver
import android.content.Context
import com.example.urnaeletrnica.model.dao.PartyDao
import com.example.urnaeletrnica.model.dao.PlateDao
import com.example.urnaeletrnica.model.entities.Plate

class DataBankPlateController (private val applicationContext: Context, private val contentResolver: ContentResolver, private val dao: PlateDao) {

    fun getPlates(){

    }
    fun deletePlate(plate: Plate) = dao.deletePlate(plate)
    fun getPlatesDto() = dao.getPlatesDto()

    fun getPlateDtoById(id:Int)= dao.getPlateDtoById(id)

    fun getPlatesById(id:Int) = dao.getPlatesById(id);

    fun insertPlate(mainCandidateId:Int,viceCandidateId:Int,officeId: Int,plateName:String):Plate{
        val plate = Plate(
            mainId = mainCandidateId,
            viceId = viceCandidateId,
            officeId = officeId,
            plateName = plateName
        )
        dao.insertPlate(plate)
        return plate;
    }
    fun getPlateDtoByPartyOfMainCandidateAndOfficeId(partyId:Int,officeId: Int) = dao.getPlateDtoByPartyOfMainCandidateAndOfficeId(partyId, officeId)

}
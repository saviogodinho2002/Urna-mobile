package com.example.urnaeletrnica.controllers

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.example.urnaeletrnica.model.dao.AppDataBase
import com.example.urnaeletrnica.model.entities.Office
import com.example.urnaeletrnica.model.entities.Party
import com.example.urnaeletrnica.model.entities.Section
import com.example.urnaeletrnica.model.entities.Zone
import com.example.urnaeletrnica.model.relationship.SectionAndZone
import com.example.urnaeletrnica.model.relationship.ZoneAndSections
import java.lang.IllegalArgumentException


class DataBankGeralController(private val applicationContext: Context, private val contentResolver: ContentResolver, private val db:AppDataBase) {
    private val dataBankZoneController = DataBankZoneController(applicationContext,contentResolver,db.ZoneDao())
    private val dataBankOfficeController = DataBankOfficeController(applicationContext,contentResolver,db.OfficeDao())
    private val dataBankPartyController = DataBankPartyController(applicationContext,contentResolver,db.PartyDao())
    private val dataBankSectionController = DataBankSectionController(applicationContext,contentResolver,db.SectionDao())

    fun getOffices():List<Office>{
        return dataBankOfficeController.getOffices();
    }
    fun deleteOffice(office: Office){
        dataBankOfficeController.deleteOffice(office);
    }
    fun saveOffice(nameOffice:String,number:Int, executive:Boolean): Office {
        return  dataBankOfficeController.saveOffice(nameOffice,number,executive)
    }
    private fun existSomePartyWithInitials(initials:String):Boolean{
        return  dataBankPartyController.existSomePartyWithInitials(initials)
    }

    fun getPartys():List<Party>{
        return dataBankPartyController.getPartys();
    }


    fun saveParty(imgUri: Uri?, partyName:String, partyInitials:String, partyNumber:String): Party {

        return dataBankPartyController.saveParty(imgUri,partyName,partyInitials,partyNumber);

    }
    fun deleteParty(party: Party){
        dataBankPartyController.deleteParty(party)
    }

    fun updateParty(oldParty: Party, imgUri: Uri?, partyName:String, partyInitials:String, partyNumber:String): Party {

        return dataBankPartyController.updateParty(oldParty,imgUri,partyName,partyInitials,partyNumber);
    }
    fun getSectionAndZone():List<ZoneAndSections> = dataBankZoneController.getSectionAndZone()
    fun getZones():List<Zone>{
        return dataBankZoneController.getZones()
    }
    fun deleteZone(zone: Zone){
        dataBankZoneController.deleteZone(zone)
    }
    fun getZoneByNumber(number: String):Zone = dataBankZoneController.getZoneByNumber(number)

    fun getZoneNumbers():List<String> = dataBankZoneController.getZoneNumbers()

    private fun existAnotherZoneWithNumber(number: String):Boolean{
        return dataBankZoneController.existAnotherZoneWithNumber(number)
    }

    fun saveZone(nameZone:String,number:String): Zone {

        return dataBankZoneController.saveZone(nameZone,number);
    }
    /////////////// classes com relacionamento

    private fun getZoneById(zoneId: Int) = dataBankZoneController.getZoneById(zoneId)

    fun getSections():List<Section>{
        return dataBankSectionController.getSections();
    }
    fun deleteSection(section: Section){
        dataBankSectionController.deleteSection(section);
    }

    fun saveSection(sectionNum:String, zoneNumber: String): SectionAndZone {


        val zone = getZoneByNumber(zoneNumber)
        if(!(zone is Zone))
            throw IllegalArgumentException(applicationContext.getString(com.example.urnaeletrnica.R.string.not_exist_zone_with_number))

        return SectionAndZone(zone, dataBankSectionController.saveSection(sectionNum,zone.id) )
    }

}
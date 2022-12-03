package com.example.urnaeletrnica.controllers

import android.content.ContentResolver
import android.content.Context

import com.example.urnaeletrnica.model.dao.ZoneDao

import com.example.urnaeletrnica.model.entities.Zone

class DataBankZoneController (private val applicationContext: Context, private val contentResolver: ContentResolver, private val dao: ZoneDao) {
    fun getZones():List<Zone>{
        return dao.getZones();
    }
    fun deleteZone(zone: Zone){
        dao.deleteZone(zone);
    }
    fun existAnotherZoneWithNumber(number: String):Boolean{
        return dao.getZoneByNumber(number) is Zone
    }
    fun saveZone(nameZone:String,number:String): Zone {
        if (existAnotherZoneWithNumber(number)){
            throw Exception(applicationContext.getString(com.example.urnaeletrnica.R.string.exist_another_zone) )
        }
        val zone = Zone(
            zoneNumber =  number,
            zoneName = nameZone)
        dao.insertZone(
            zone
        )
        return zone;
    }
}
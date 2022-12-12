package com.example.urnaeletrnica.model.dao

import androidx.room.*
import com.example.urnaeletrnica.model.entities.Zone


@Dao
interface ZoneDao {
    @Insert
    fun insertZone(zone:Zone)

    @Query("SELECT * FROM Zone")
    fun getZones():List<Zone>

    @Query("SELECT * FROM Zone WHERE id = :id")
    fun getZoneById(id:Int):Zone

    @Query("SELECT * FROM ZONE WHERE zoneNumber = :number")
    fun getZoneByNumber(number:String):Zone

    @Query("SELECT zoneNumber FROM zone")
    fun getZonesNumber():List<String>

    @Delete
    fun deleteZone(zone:Zone):Int

    @Update
    fun updateZone(zone:Zone)



}
package com.example.urnaeletrnica.model.dao

import androidx.room.*

import com.example.urnaeletrnica.model.entities.Office


@Dao
interface OfficeDao {

    @Insert
    fun insertOffice(office: Office)

    @Query("SELECT * FROM Office")
    fun getOffices():List<Office>

    @Query("SELECT * FROM Office Where name = :name") //variavel dinamica, mesma da função de baixo
    fun getOfficeByName(name:String): Office;

    @Query("SELECT * FROM Office Where id = :id") //variavel dinamica, mesma da função de baixo
    fun getOfficeByID(id:Int): Office;

    @Query("SELECT * FROM Office Where isExecutive = 1")
    fun getOfficesExecutive():List<Office>

    @Query("SELECT * FROM Office Where isExecutive = 0")
    fun getOfficesIsNotExecutive():List<Office>

    @Query("SELECT * FROM Office where isExecutive = 1 and (select count(*) from Plate join Candidate on Candidate.officeId = Office.id and (Plate.mainId = Candidate.id or  Plate.viceId = Candidate.id) )")
    fun getOfficesExecutiveHasPlate():List<Office>

    @Query("SELECT * FROM Office where isExecutive = 0 and (select count(*) from Candidate where Candidate.officeId = Office.id )")
    fun getOfficesIsNotExecutiveHasCandidate():List<Office>



    @Delete
    fun deleteOffice(office: Office): Int

    @Update
    fun updateOffice(office: Office)

}
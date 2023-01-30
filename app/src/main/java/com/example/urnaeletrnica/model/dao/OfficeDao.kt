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

    @Delete
    fun deleteOffice(office: Office): Int

    @Update
    fun updateOffice(office: Office)

}
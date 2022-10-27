package com.example.urnaeletrnica.model.dao

import androidx.room.*
import com.example.urnaeletrnica.model.entities.Plate

@Dao
interface PlateDao {
    @Insert
    fun insertPlate(plate: Plate)

    @Query("SELECT * FROM Plate")
    fun getPlateRegisters():List<Plate>;

    @Query("SELECT * FROM Plate Where id = :id") //variavel dinamica, mesma da função de baixo
    fun getPlateByID(id:Int): Plate;

    @Delete
    fun deletePlate(plate: Plate): Int

    @Update
    fun updatePlate(plate: Plate)

}
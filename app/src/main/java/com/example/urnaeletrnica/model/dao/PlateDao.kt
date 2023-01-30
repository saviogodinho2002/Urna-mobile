package com.example.urnaeletrnica.model.dao

import androidx.room.*
import com.example.urnaeletrnica.model.entities.Plate
import com.example.urnaeletrnica.model.relationship.PlateDto

@Dao
interface PlateDao {
    @Insert
    fun insertPlate(plate: Plate)

    @Query("SELECT * FROM Plate")
    fun getPlateRegisters():List<Plate>;

    @Query("SELECT * FROM Plate Where id = :id") //variavel dinamica, mesma da função de baixo
    fun getPlateByID(id:Int): Plate;

    @Query("SELECT Plate.id as id, Voter.name as mainName, Voter.name as viceName from Plate join Candidate join Voter on (Candidate.voterID = Voter.id AND Plate.mainId = Candidate.id) ") //variavel dinamica, mesma da função de baixo
    fun getPlatesDto(): List<PlateDto>;

    @Delete
    fun deletePlate(plate: Plate): Int

    @Update
    fun updatePlate(plate: Plate)

}
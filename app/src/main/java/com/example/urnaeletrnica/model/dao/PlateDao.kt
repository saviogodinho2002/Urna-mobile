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

    @Query("SELECT Party.name as partyName, Party.initials as partyInitials, Party.number as partyNumber, Party.logoPhoto as partyPhotoUrl, Plate.plateName as plateName from Plate join Candidate on (Plate.mainId = Candidate.id  and Candidate.officeId = Plate.office_id) join Party on Candidate.partyId = Party.id") //variavel dinamica, mesma da função de baixo
    fun getPlatesDto(): List<PlateDto>;

    /*@Query("SELECT Plate.*,Party.* from Plate join Candidate on (Plate.mainId = Candidate.id  and Candidate.officeId = Plate.office_id and Plate.id = :id) join Party on Candidate.partyId = Party.id ") //variavel dinamica, mesma da função de baixo
    fun getPlateDtoById(id:Int): PlateDto;*/

    @Delete
    fun deletePlate(plate: Plate): Int

    @Update
    fun updatePlate(plate: Plate)

}
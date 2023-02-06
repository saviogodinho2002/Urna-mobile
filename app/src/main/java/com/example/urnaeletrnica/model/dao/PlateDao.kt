package com.example.urnaeletrnica.model.dao

import androidx.room.*
import com.example.urnaeletrnica.model.entities.Office
import com.example.urnaeletrnica.model.entities.Plate
import com.example.urnaeletrnica.model.relationship.PlateDto

@Dao
interface PlateDao {
    @Insert
    fun insertPlate(plate: Plate)

    @Query("SELECT * FROM Plate")
    fun getPlateRegisters():List<Plate>;

    @Query("SELECT * FROM Plate Where id = :id")
    fun getPlatesById(id: Int):Plate

    @Query("SELECT * FROM Plate Where id = :id") //variavel dinamica, mesma da função de baixo
    fun getPlateByID(id:Int): Plate;

    @Query("SELECT Plate.id as id,Party.name as partyName, Party.initials as partyInitials, Party.number as partyNumber,Candidate.officeId as officeId, Party.logoPhoto as partyPhotoUrl, Plate.plateName as plateName, Plate.mainId as mainId, Plate.viceId as viceId from Plate join Candidate on (Plate.mainId = Candidate.id  and Candidate.officeId = Plate.office_id) join Party on Candidate.partyId = Party.id") //variavel dinamica, mesma da função de baixo
    fun getPlatesDto(): List<PlateDto>;

    @Query("SELECT Plate.id as id,Party.name as partyName, Party.initials as partyInitials, Party.number as partyNumber,Candidate.officeId as officeId, Party.logoPhoto as partyPhotoUrl, Plate.plateName as plateName, Plate.mainId as mainId, Plate.viceId as viceId from Plate join Candidate on (Plate.mainId = Candidate.id  and Candidate.officeId = Plate.office_id) join Party on Candidate.partyId = Party.id and Plate.id = :id") //variavel dinamica, mesma da função de baixo
    fun getPlateDtoById(id:Int): PlateDto;

    @Query("SELECT Plate.id as id,Party.name as partyName, Party.initials as partyInitials, Party.number as partyNumber,Candidate.officeId as officeId, Party.logoPhoto as partyPhotoUrl, Plate.plateName as plateName, Plate.mainId as mainId, Plate.viceId as viceId " +
            "from Plate join Candidate on (Plate.mainId = Candidate.id  and Candidate.officeId = Plate.office_id and Candidate.officeId = :officeId) join Party on Candidate.partyId = Party.id and Party.id = :partyId") //variavel dinamica, mesma da função de baixo
    fun getPlateDtoByPartyOfMainCandidateAndOfficeId(partyId:Int, officeId:Int):PlateDto

    @Delete
    fun deletePlate(plate: Plate): Int

    @Update
    fun updatePlate(plate: Plate)

}
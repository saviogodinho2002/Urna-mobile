package com.example.urnaeletrnica.controllers

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.example.urnaeletrnica.model.dao.AppDataBase
import com.example.urnaeletrnica.model.entities.*
import com.example.urnaeletrnica.model.relationship.PlateDto
import com.example.urnaeletrnica.model.relationship.SectionAndVoters
import com.example.urnaeletrnica.model.relationship.SectionAndZone
import com.example.urnaeletrnica.model.relationship.ZoneAndSections
import com.example.urnaeletrnica.utils.InternalPhotosController
import java.lang.IllegalArgumentException


class DataBankGeralController(private val applicationContext: Context, private val contentResolver: ContentResolver, private val db:AppDataBase) {
    private val dataBankZoneController = DataBankZoneController(applicationContext,contentResolver,db.ZoneDao())
    private val dataBankOfficeController = DataBankOfficeController(applicationContext,contentResolver,db.OfficeDao())
    private val dataBankPartyController = DataBankPartyController(applicationContext,contentResolver,db.PartyDao())
    private val dataBankSectionController = DataBankSectionController(applicationContext,contentResolver,db.SectionDao())
    private val dataBankVoterController = DataBankVoterController(applicationContext,contentResolver,db.VoterDao())
    private val dataBankCandidateController = DataBankCandidateController(applicationContext,contentResolver,db.CandidateDao())
    private val dataBankPlateController = DataBankPlateController(applicationContext,contentResolver,db.PlateDao())
    private val dataBankVotesElectionController = DataBankVotesElectionController(applicationContext,contentResolver,db.VotesElectionDao())

    private val directoryPartyPhotos = "party_photos";
    private val directoryVoterPhotos = "voter_photos";


    fun getOffices():List<Office>{
        return dataBankOfficeController.getOffices();
    }
    fun deleteOffice(office: Office){
        dataBankOfficeController.deleteOffice(office);
    }
    fun saveOffice(nameOffice:String,number:Int, executive:Boolean): Office {
        return  dataBankOfficeController.saveOffice(nameOffice,number,executive)
    }
    fun getOfficesExecutive() = dataBankOfficeController.getOfficesExecutive()
    fun getOfficesIsNotExecutive() = dataBankOfficeController.getOfficesIsNotExecutive()

    fun getOfficesIsNotExecutiveHasCandidate() = dataBankOfficeController.getOfficesIsNotExecutiveHasCandidate()
    fun getOfficesExecutiveHasPlate() = dataBankOfficeController.getOfficesExecutiveHasPlate()

    fun totalValidVotesToOfficeNotExecutive(officeId: Int):Int {
      return dataBankVotesElectionController.totalValidVotesToOfficeNotExecutive(officeId)
    }
    fun totalValidVotesToOfficeNotExecutiveOnSection(officeId: Int,sectionId: Int) = dataBankVotesElectionController.totalValidVotesToOfficeExecutiveMatchCandidateOnSection(officeId,sectionId)
    fun totalValidVotesToOfficeExecutive(officeId: Int):Int {
        return dataBankVotesElectionController.totalValidVotesToOfficeExecutive(officeId)
    }
    fun totalValidVotesToOfficeExecutiveOnSection(officeId: Int,sectionId: Int):Int {
        return dataBankVotesElectionController.totalValidVotesToOfficeExecutiveOnSection(officeId,sectionId)
    }
    fun totalVotes() = dataBankVotesElectionController.totalVotes()
    private fun existSomePartyWithInitials(initials:String):Boolean{
        return  dataBankPartyController.existSomePartyWithInitials(initials)
    }

    fun getPartys():List<Party>{
        return dataBankPartyController.getPartys();
    }


    fun saveParty(imgUri: Uri?, partyName:String, partyInitials:String, partyNumber:String): Party {
        var imgDirectory:String? = null

        if(imgUri != null){
            imgDirectory =  InternalPhotosController.saveAndGetDirPhoto( applicationContext ,contentResolver,directoryPartyPhotos,imgUri!! )
        }
        return dataBankPartyController.saveParty(imgDirectory,partyName,partyInitials,partyNumber);

    }
    fun deleteParty(party: Party){
        dataBankPartyController.deleteParty(party)
    }

    fun updateParty(oldParty: Party, imgUri: Uri?, partyName:String, partyInitials:String, partyNumber:String): Party {
        var imgDirectory:String? = null;
        if(imgUri != null){
            imgDirectory = InternalPhotosController.saveAndGetDirPhoto( applicationContext ,contentResolver,directoryPartyPhotos,imgUri!! )
        }else if(oldParty.logoPhoto != null){
            imgDirectory = oldParty.logoPhoto
        }
        return dataBankPartyController.updateParty(oldParty,imgDirectory,partyName,partyInitials,partyNumber);
    }
    fun getZoneAndSections():List<ZoneAndSections> = dataBankZoneController.getZoneAndSections()
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

     fun getZoneById(zoneId: Int) = dataBankZoneController.getZoneById(zoneId)

    fun getSections():List<Section>{
        return dataBankSectionController.getSections();
    }
    fun deleteSection(section: Section){
        dataBankSectionController.deleteSection(section);
    }
    fun getSectionAndVoters():List<SectionAndVoters> = dataBankSectionController.getSectionAndVoters()
    fun saveSection(sectionNum:String, zoneNumber: String): SectionAndZone {


        val zone = getZoneByNumber(zoneNumber)
        if(!(zone is Zone))
            throw IllegalArgumentException(applicationContext.getString(com.example.urnaeletrnica.R.string.not_exist_zone_with_number))

        return SectionAndZone(zone, dataBankSectionController.saveSection(sectionNum,zone.id) )
    }

     fun saveVoter(voterName:String,voterTittle:String, sectionId:Int,photoUri:Uri?):Voter{
        var photoDirectory:String? = null

        if(photoUri != null){
            photoDirectory =  InternalPhotosController.saveAndGetDirPhoto( applicationContext ,contentResolver,directoryVoterPhotos,photoUri )
        }
        return  dataBankVoterController.saveVoter(voterName,voterTittle,sectionId,photoDirectory);

    }
    fun deleteVoter(voter: Voter) = dataBankVoterController.deleteVoter(voter)
    fun getZoneBySectionId(sectionId: Int):Zone =  dataBankZoneController.getZoneById(  dataBankSectionController.getSectionById(sectionId).zoneId )

    fun getVoters():List<Voter> = dataBankVoterController.getVoters()

    fun getVoterByTittle(tittle:String) = dataBankVoterController.getVoterByTittle(tittle)

    fun auntenticateVoterWithTittle(tittle:String, sectionId: Int):Int{
        val voter = getVoterByTittle(tittle)
        if(voter !is Voter)
            throw Exception( applicationContext.getString( com.example.urnaeletrnica.R.string.incorrect_tittle))
        if (voter.sectionId != sectionId)
            throw Exception( applicationContext.getString( com.example.urnaeletrnica.R.string.incorrect_session))

        val votesElection = dataBankVotesElectionController.getVotesElectionByVoterId(voter.id)
        if(votesElection.isNotEmpty())
            throw Exception( applicationContext.getString( com.example.urnaeletrnica.R.string.voter_has_voted))

        return voter.id
    }

    fun getCandidateByVoterId(voterId: Int) = dataBankCandidateController.getCandidateByVoterId(voterId)
    fun saveCandidate(voterId:Int,officeId:Int,partyId:Int,numberCandidate: String?):Candidate {
        if(getCandidateByVoterId(voterId) is Candidate)
            throw Exception(applicationContext.getString(com.example.urnaeletrnica.R.string.candidate_already_exist))
        return dataBankCandidateController.saveCandidate(voterId,officeId,partyId,numberCandidate)
    }
    fun getCandidatesDto() = dataBankCandidateController.getCandidatesDto()

    fun getCandidates() = dataBankCandidateController.getCandidates()
    fun getCandidateById(id:Int) = dataBankCandidateController.getCandidateById(id)
    fun deleteCandidate(candidate: Candidate){
        dataBankCandidateController.deleteCandidate(candidate)
    }
    fun getCandidateDtoByVoterId(voterId: Int) = dataBankCandidateController.getCandidateDtoByVoterId(voterId)

    fun getVoterByCandidateId(id:Int) = dataBankVoterController.getVoterByCandidateId(id)

    fun getCandidateByNumber(number:String) = dataBankCandidateController.getCandidateByNumber(number)

    fun getCandidateDtoByNumber(number: String) = dataBankCandidateController.getCandidateDtoByNumber(number)

    fun getCandidateDtoByNumberAndOffice(number: String,officeId: Int) = dataBankCandidateController.getCandidateDtoByNumberAndOffice(number,officeId)

    ///plate
    fun savePlate(mainCandidateId:Int,viceCandidateId:Int,officeId: Int,plateName:String)= dataBankPlateController.insertPlate(mainCandidateId,viceCandidateId,officeId,plateName);

    fun getPlatesDto() = dataBankPlateController.getPlatesDto()

    fun getPlateDtoById(id:Int) = dataBankPlateController.getPlateDtoById(id)

    fun getPlatesById(id:Int) = dataBankPlateController.getPlatesById(id)

    fun deletePlate(plate: Plate) = dataBankPlateController.deletePlate(plate)
    fun getPlateDtoByPartyOfMainCandidateAndOfficeId(partyId:Int,officeId: Int) = dataBankPlateController.getPlateDtoByPartyOfMainCandidateAndOfficeId(partyId,officeId)
    fun getPlateDtoByPartyNumber(number: String,officeId: Int):PlateDto{
        val party = dataBankPartyController.getPartyByNumber(number)
        if(party is Party)
            return getPlateDtoByPartyOfMainCandidateAndOfficeId(party.id,officeId)
        throw Exception(applicationContext.getString(com.example.urnaeletrnica.R.string.non_existe_party))
    }
    fun saveVotesElections(votesElection: List<VotesElection>){
        votesElection.forEach {
            dataBankVotesElectionController.saveVoteElection(it)
        }

    }
    fun truncateVotesElections() = dataBankVotesElectionController.truncateVotesElections()
    fun getVotesSections() = dataBankVotesElectionController.getVotesSections()

    fun getVotesElectionsOfUrn(sectionId:Int) = dataBankVotesElectionController.getVotesElectionsOfUrn(sectionId)

    fun getVotesElectionsToNumber(number:String, officeId: Int) = dataBankVotesElectionController.getVotesSectionsToNumber(number,officeId)
    fun getVotesElectionsToNumberOnSection(number:String, officeId: Int,sectionId: Int) = dataBankVotesElectionController.getVotesSectionsToNumberOnSection(number,officeId,sectionId)

    fun getVotesElectionsToNumberExecutive(number:String, officeId: Int) = dataBankVotesElectionController.getVotesSectionsToNumberExecutive(number,officeId)
    fun getVotesElectionsToNumberExecutiveOnSection(number:String, officeId: Int,sectionId: Int) = dataBankVotesElectionController.getVotesSectionsToNumberExecutiveOnSection(number,officeId,sectionId)

}
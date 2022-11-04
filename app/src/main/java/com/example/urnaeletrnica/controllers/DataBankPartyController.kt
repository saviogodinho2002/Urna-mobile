package com.example.urnaeletrnica.controllers

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.example.urnaeletrnica.App
import com.example.urnaeletrnica.model.dao.PartyDao
import com.example.urnaeletrnica.model.entities.Party
import com.example.urnaeletrnica.utils.InternalPhotosController

class DataBankPartyController(private val applicationContext:Context,private val contentResolver:ContentResolver, private val dao:PartyDao) {
    private val directory = "party_photos";

    fun getPartys():List<Party>{
        val response = dao.getPartRegisters();
        return  response;
    }
    fun saveParty(imgUri:Uri?, partyName:String,partyInitials:String,partyNumber:String):Party{

        var imgDirectory:String? = null

        if(imgUri != null){
            //imgDirectory = saveAndGetDirPhoto(imgUri!!)
            imgDirectory =  InternalPhotosController.saveAndGetDirPhoto( applicationContext ,contentResolver,directory,imgUri!! )
        }
        val party = Party(
                name = partyName,
                logoPhoto = imgDirectory,
                initials =  partyInitials,
                number = partyNumber)

        dao.insertParty(party)

        return party;

    }
    fun deleteParty(party:Party){

        if (party.logoPhoto != null)
            InternalPhotosController.removePhotoFile(party.logoPhoto)

        dao.deleteParty(party)

    }
    fun updateParty(oldParty:Party,imgUri:Uri?, partyName:String,partyInitials:String,partyNumber:String):Party{
        var imgDirectory:String? = null;
        if(imgUri != null){
            imgDirectory = InternalPhotosController.saveAndGetDirPhoto( applicationContext ,contentResolver,directory,imgUri!! )
        }else if(oldParty.logoPhoto != null){
            imgDirectory = oldParty.logoPhoto
        }

        val newParty = Party(
            id = oldParty.id,
            name = partyName,
            logoPhoto = imgDirectory,
            initials =  partyInitials,
            number = partyNumber)


        if( (oldParty.logoPhoto != null) && ((imgDirectory == null) || (oldParty.logoPhoto != imgDirectory)) )
            InternalPhotosController.removePhotoFile(oldParty.logoPhoto)


        dao.updateParty(newParty)
        return newParty;
    }
}
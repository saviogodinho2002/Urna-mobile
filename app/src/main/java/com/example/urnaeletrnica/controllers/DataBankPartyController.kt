package com.example.urnaeletrnica.controllers

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.urnaeletrnica.model.dao.PartyDao
import com.example.urnaeletrnica.model.entities.Party
import com.example.urnaeletrnica.utils.InternalPhotosController
import kotlin.Exception

class DataBankPartyController(private val applicationContext:Context,private val contentResolver:ContentResolver, private val dao:PartyDao) {

    fun existSomePartyWithInitials(initials:String):Boolean{
        return  dao.getPartyByInitials(initials) is Party
    }

    fun getPartys():List<Party>{
        val response = dao.getPartyRegisters();
        return  response;
    }

    fun getPartyByNumber(number:String) = dao.getPartyByNumber(number)


    fun saveParty(imgDirectory:String?, partyName:String,partyInitials:String,partyNumber:String):Party{

        if(existSomePartyWithInitials(partyInitials.uppercase())){
            throw Exception( applicationContext.resources.getString(com.example.urnaeletrnica.R.string.already_exist_party_signals))

        }
        val party = Party(
                name = partyName,
                logoPhoto = imgDirectory,
                initials =  partyInitials.uppercase(),
                number = partyNumber)

        dao.insertParty(party)

        return party;

    }
    fun deleteParty(party:Party){

        if (party.logoPhoto != null)
            InternalPhotosController.removePhotoFile(party.logoPhoto)

        dao.deleteParty(party)

    }
    fun updateParty(oldParty:Party,imgDirectory:String?, partyName:String,partyInitials:String,partyNumber:String):Party{

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
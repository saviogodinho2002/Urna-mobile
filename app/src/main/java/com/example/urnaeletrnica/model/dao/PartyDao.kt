package com.example.urnaeletrnica.model.dao

import androidx.room.*
import com.example.urnaeletrnica.model.entities.Candidate
import com.example.urnaeletrnica.model.entities.Party


@Dao
interface PartyDao {
    @Insert
    fun insertParty(party: Party)

    @Query("SELECT * FROM Party")
    fun getPartyRegisters():List<Party>;

    @Query("SELECT * FROM PARTY WHERE initials = :initials")
    fun getPartyByInitials(initials:String):Party

    @Query("SELECT * FROM Party Where id = :id") //variavel dinamica, mesma da função de baixo
    fun getPartyByID(id:Int): Party;
    @Query("SELECT * FROM Party Where number = :number") //variavel dinamica, mesma da função de baixo
    fun getPartyByNumber(number:String): Party;


    @Delete
    fun deleteParty(party: Party): Int

    @Update
    fun updateParty(party: Party)

}
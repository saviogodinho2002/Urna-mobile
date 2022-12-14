package com.example.urnaeletrnica.model.dao

import androidx.room.*
import com.example.urnaeletrnica.model.entities.Section

@Dao
interface SectionDao {
    @Insert
    fun insertSection(section: Section)

    @Query("SELECT * FROM Section")
    fun getSections():List<Section>

    @Query("SELECT * FROM Section WHERE id = :id")
    fun getSectionById(id:Int):Section

    @Query("SELECT * FROM Section WHERE sectionNumber = :number")
    fun getSectionByNumber(number: String):Section



    @Delete
    fun deleteSection(section: Section):Int

    @Update
    fun updateSection(section: Section)
}
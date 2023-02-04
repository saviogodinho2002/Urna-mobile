package com.example.urnaeletrnica

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.urnaeletrnica.controllers.DataBankGeralController
import com.example.urnaeletrnica.model.entities.Voter
import com.example.urnaeletrnica.model.relationship.SectionAndZone
import java.util.TreeMap

class AutenticateVoter : AppCompatActivity() {

    private lateinit var dropSections: AutoCompleteTextView
    private lateinit var mapSectionAndZone: MutableMap<String, SectionAndZone>
    private lateinit var controller: DataBankGeralController

    private lateinit var btnAutenticateVoter:Button
    private lateinit var editTittle:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autenticate_voter)

        dropSections = findViewById(R.id.auto_zone_sections)

        val app = application as App
        controller = DataBankGeralController(applicationContext,contentResolver,app.db)

        btnAutenticateVoter = findViewById(R.id.btn_autenticate)
        editTittle = findViewById(R.id.autenticate_tittle)

        btnAutenticateVoter.setOnClickListener {
            auntenticate()
        }

        fetchSectionsOnDrop()
    }
    private fun auntenticate(){
        if(editTittle.text.toString().isEmpty() || editTittle.text.toString().length < 5)
            return;
        Thread{
            try {
                val sectionAndZone = mapSectionAndZone[dropSections.text.toString()]
                val voter_id = controller.auntenticateVoterWithTittle( editTittle.text.toString(),sectionAndZone!!.section!!.id );
                runOnUiThread {

                    val intent = Intent(this@AutenticateVoter,Urn::class.java)

                    intent.putExtra("voter_id",voter_id)
                    startActivity(intent)

                }

            }catch (error:Exception){
                runOnUiThread{
                    Toast.makeText(this@AutenticateVoter,error.message.toString(),Toast.LENGTH_LONG).show()
                }
            }


        }.start()

    }
    private  fun fetchSectionsOnDrop(){
        Thread{
            mapSectionAndZone = mutableMapOf()
            val items = controller.getZoneAndSections()   // daoZone.getZonesNumber()
            items.forEach { zoneAndSections ->
                zoneAndSections.getSectionAndZone().forEach {sectionAndZone->
                    val key = "${sectionAndZone.zone!!.zoneNumber} - ${sectionAndZone.section!!.sectionNumber}".trim()
                    mapSectionAndZone[key] = sectionAndZone
                }
            }
            runOnUiThread{

                val adapterDrop = ArrayAdapter(this,android.R.layout.simple_list_item_1, mapSectionAndZone.keys.toList() )
                if (mapSectionAndZone.isNotEmpty())
                    dropSections.setText(mapSectionAndZone.keys.first())
                dropSections.setAdapter(adapterDrop)

            }
        }.start()
    }
}
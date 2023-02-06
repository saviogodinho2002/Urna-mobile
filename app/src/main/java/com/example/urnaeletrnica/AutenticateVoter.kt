package com.example.urnaeletrnica

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.urnaeletrnica.controllers.DataBankGeralController
import com.example.urnaeletrnica.model.relationship.SectionAndZone

class AutenticateVoter : AppCompatActivity() {

    private lateinit var dropSections: AutoCompleteTextView
    private lateinit var mapSectionAndZone: MutableMap<String, SectionAndZone>
    private lateinit var controller: DataBankGeralController

    private lateinit var btnAutenticateVoter:Button
    private lateinit var btnResetElection:Button
    private lateinit var editTittle:EditText
    private lateinit var btnReportThis:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autenticate_voter)

        dropSections = findViewById(R.id.auto_zone_sections)

        val app = application as App
        controller = DataBankGeralController(applicationContext,contentResolver,app.db)

        btnAutenticateVoter = findViewById(R.id.btn_autenticate)
        btnResetElection = findViewById(R.id.btn_reset)
        btnReportThis = findViewById(R.id.btn_report_this)
        editTittle = findViewById(R.id.autenticate_tittle)

        btnAutenticateVoter.setOnClickListener {
            auntenticate()
        }

        fetchSectionsOnDrop()

        btnResetElection.setOnClickListener {
            resetElection()
        }
        btnReportThis.setOnClickListener {
            gerateReportOfThisUrn()
        }
    }
    private fun resetElection(){
        Thread{
            controller.truncateVotesElections()
        }.start()
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

    private fun gerateUrn(){
        Thread{
            val votes = controller.getVotesSections()



        }.start()
    }
    private fun gerateReportOfThisUrn(){
        Thread{
            val sectionAndZone = mapSectionAndZone[dropSections.text.toString()]
            val votes = controller.getVotesElectionsOfUrn(sectionAndZone!!.section!!.id)
            val officesNotExecutive = controller.getOfficesIsNotExecutiveHasCandidate()
            val officeIsExecutive = controller.getOfficesExecutiveHasPlate()
            val report = StringBuilder()
            report.append("Zona: ${sectionAndZone.zone!!.zoneNumber}\n")
            report.append("Urna: ${sectionAndZone.section!!.sectionNumber}\n")

            ///todo total votos
            ///todo total votos para cada cargo
            ///todo total votos pra candidato X
            var totalValid = 0
            report.append("\n----- Votos Válidos -----\n\n")
            report.append("---------------------------------\n")
            report.append("Cargos Não Executivos\n")
            officesNotExecutive.forEach { office->
                val current = controller.totalValidVotesToOfficeNotExecutive(office.id)
                totalValid += current
                report.append("${office.name} : ${current} \n")
            }

            report.append("\n---------------------------------\n")
            report.append("Cargos Executivos\n")
            officeIsExecutive.forEach {office ->
                val current = controller.totalValidVotesToOfficeExecutive(office.id)
                totalValid += current
                report.append("${office.name} : ${current} \n")

            }
            val total = controller.totalVotes()
            report.append("\n---------------------------------\n")
            report.append("Cargos Executivos\n")
            report.append("Total: ${total}\n")
            report.append("Total válidos: ${totalValid}\n")
            report.append("Total inválidos: ${total - totalValid}\n")

            Log.i("reports",report.toString())
        }.start()
    }
}
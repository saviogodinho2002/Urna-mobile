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
    private lateinit var btnReportAll:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autenticate_voter)

        dropSections = findViewById(R.id.auto_zone_sections)

        val app = application as App
        controller = DataBankGeralController(applicationContext,contentResolver,app.db)

        btnAutenticateVoter = findViewById(R.id.btn_autenticate)
        btnResetElection = findViewById(R.id.btn_reset)
        btnReportThis = findViewById(R.id.btn_report_this)
        btnReportAll = findViewById(R.id.btn_report_all)
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
        btnReportAll.setOnClickListener {
            gerateReporAllUrn()
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

    private fun gerateReporAllUrn(){
        Thread{
            //val sectionAndZone = mapSectionAndZone[dropSections.text.toString()]
            val votes = controller.getVotesSections()///controller.getVotesElectionsOfUrn(sectionAndZone!!.section!!.id)
            val officesNotExecutive = controller.getOfficesIsNotExecutiveHasCandidate()
            val officeIsExecutive = controller.getOfficesExecutiveHasPlate()
            val report = StringBuilder()
            //report.append("Zona: ${sectionAndZone!!.zone!!.zoneNumber}\n")
            //report.append("Urna: ${sectionAndZone.section!!.sectionNumber}\n")

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
            val total = votes.size//controller.totalVotes()
            report.append("\n---------------------------------\n")
            report.append("Cargos Executivos\n")
            report.append("Total de votos: ${total}\n")
            report.append("Total de votos válidos: ${totalValid}\n")
            report.append("Total de votos brancos ou nulos: ${total - totalValid}\n")

            report.append("\n---------------------------------\n")

            val candidatesDto = controller.getCandidatesDto()

            ///rodando pra nao executivo
            report.append("\n----- Votos de cada Candidato -----\n\n")
            officesNotExecutive.forEach { office->
                report.append("Cargo: ${office.name}\n\n")
                candidatesDto.forEach {candidateDto ->
                    if(candidateDto.officeId == office.id && !candidateDto.isExecutive){
                        val currentVotes = controller.getVotesElectionsToNumber(candidateDto.numberCandidate, officeId = office.id)
                        report.append("* Nome: ${candidateDto.voterName}\n")
                        report.append("* Votos: ${currentVotes}\n")
                        report.append("\n")
                    }

                }
                report.append("##########################\n")
            }
            ///rodando para executivos
            val platesDto = controller.getPlatesDto()
            report.append("\n----- Votos de cada Chapa -----\n\n")
            officeIsExecutive.forEach { office->
                report.append("Chapa para: ${office.name}\n\n")

                platesDto.forEach {plateDto ->
                    if(plateDto.officeId == office.id ){
                        val currentVotes = controller.getVotesElectionsToNumberExecutive(plateDto.partyNumber, officeId = office.id)
                        report.append("* Principal: ${controller.getVoterByCandidateId(plateDto.mainId).name}\n")
                        report.append("* Vice: ${controller.getVoterByCandidateId(plateDto.viceId).name}\n")
                        report.append("* Votos: ${currentVotes}\n")
                        report.append("\n")
                    }

                }
                report.append("##########################\n")
            }

            Log.i("reports",report.toString())
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
                val current = controller.totalValidVotesToOfficeNotExecutiveOnSection(office.id,sectionAndZone.section.id)
                totalValid += current
                report.append("${office.name} : ${current} \n")
            }

            report.append("\n---------------------------------\n")
            report.append("Cargos Executivos\n")
            officeIsExecutive.forEach {office ->
                val current = controller.totalValidVotesToOfficeExecutiveOnSection(office.id, sectionAndZone.section.id)
                totalValid += current
                report.append("${office.name} : ${current} \n")

            }
            val total = votes.size
            report.append("\n---------------------------------\n")
            report.append("Cargos Executivos\n")
            report.append("Total de votos: ${total}\n")
            report.append("Total de votos válidos: ${totalValid}\n")
            report.append("Total de votos brancos ou nulos: ${total - totalValid}\n")

            report.append("\n---------------------------------\n")

            val candidatesDto = controller.getCandidatesDto()

            ///rodando pra nao executivo
            report.append("\n----- Votos de cada Candidato -----\n\n")
            officesNotExecutive.forEach { office->
                report.append("Cargo: ${office.name}\n\n")
                candidatesDto.forEach {candidateDto ->
                    if(candidateDto.officeId == office.id && !candidateDto.isExecutive){
                        val currentVotes = controller.getVotesElectionsToNumberOnSection(candidateDto.numberCandidate, officeId = office.id,sectionAndZone.section.id)
                        report.append("* Nome: ${candidateDto.voterName}\n")
                        report.append("* Votos: ${currentVotes}\n")
                        report.append("\n")
                    }

                }
                report.append("##########################\n")
            }
            ///rodando para executivos
            val platesDto = controller.getPlatesDto()
            report.append("\n----- Votos de cada Chapa -----\n\n")
            officeIsExecutive.forEach { office->
                report.append("Chapa para: ${office.name}\n\n")

                platesDto.forEach {plateDto ->
                    if(plateDto.officeId == office.id ){
                        val currentVotes = controller.getVotesElectionsToNumberExecutiveOnSection(plateDto.partyNumber, officeId = office.id,sectionAndZone.section.id)
                        report.append("* Principal: ${controller.getVoterByCandidateId(plateDto.mainId).name}\n")
                        report.append("* Vice: ${controller.getVoterByCandidateId(plateDto.viceId).name}\n")
                        report.append("* Votos: ${currentVotes}\n")
                        report.append("\n")
                    }

                }
                report.append("##########################\n")
            }

            Log.i("reports",report.toString())
        }.start()
    }
}
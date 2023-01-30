package com.example.urnaeletrnica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.example.urnaeletrnica.controllers.DataBankGeralController
import com.example.urnaeletrnica.model.entities.Office
import com.example.urnaeletrnica.model.relationship.CandidateDto


class PlateAdmActivity : AppCompatActivity() {

    private lateinit var dropMain:AutoCompleteTextView
    private lateinit var dropVice:AutoCompleteTextView
    private lateinit var dropOffice:AutoCompleteTextView

    private lateinit var mapCandidate:MutableMap<String, CandidateDto>
    private lateinit var mapOffice:MutableMap<String, Office>

    private lateinit var candidateList:MutableList<CandidateDto>

    private lateinit var controller:DataBankGeralController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plate_adm)
        val app = application as App

        controller = DataBankGeralController(applicationContext,contentResolver,app.db)

        dropOffice = findViewById(R.id.auto_office_plate)
        dropMain = findViewById(R.id.auto_candidate_main)
        dropVice = findViewById(R.id.auto_candidate_vice)
        candidateList = mutableListOf()
        fetchOffice()
        dropOffice.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val office = mapOffice[s.toString()]!!
                changeCandidatesByOffice(office)
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }
    private fun fetchOffice(){
        Thread{

            mapOffice = mutableMapOf()
            controller.getOfficesExecutive().forEach {
                mapOffice[it.name] = it
            }
            runOnUiThread {
                dropOffice.setText( mapOffice.keys.first().toString() )
                dropOffice.setAdapter(ArrayAdapter(this@PlateAdmActivity,android.R.layout.simple_list_item_1,mapOffice.keys.toList()))

                fetchCandidate()

            }
        }.start()
    }
    private fun fetchCandidate(){
        if(dropOffice.text.toString().isEmpty())
            return;
        Thread{
            val office = mapOffice[dropOffice.text.toString()]!!

            candidateList.addAll( controller.getCandidatesDto() )

            runOnUiThread {
                changeCandidatesByOffice(office)
            }
        }.start()
    }
    private fun changeCandidatesByOffice(currentOffice: Office){
        mapCandidate = mutableMapOf()
        candidateList.forEach {
            if(it.officeId == currentOffice.id)
                mapCandidate["${it.voterName.split(" ").first()}-${it.voterTittle}-${it.partyInitials}"] = it
        }
        dropMain.setText("")
        dropVice.setText("")
        dropMain.setAdapter(ArrayAdapter(this@PlateAdmActivity,android.R.layout.simple_list_item_1,mapCandidate.keys.toList()))
        dropVice.setAdapter(ArrayAdapter(this@PlateAdmActivity,android.R.layout.simple_list_item_1,mapCandidate.keys.toList()))

    }
}
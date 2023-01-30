package com.example.urnaeletrnica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
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

    private lateinit var btnSave:Button
    private lateinit var ediPlateName:EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plate_adm)
        val app = application as App

        controller = DataBankGeralController(applicationContext,contentResolver,app.db)

        dropOffice = findViewById(R.id.auto_office_plate)
        dropMain = findViewById(R.id.auto_candidate_main)
        dropVice = findViewById(R.id.auto_candidate_vice)
        btnSave = findViewById(R.id.btn_save_plate)
        ediPlateName = findViewById(R.id.editTextTextPlateName)

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
        btnSave.setOnClickListener {
            savePlate();
        }
    }
    private fun savePlate(){
        Thread{
            val candidateMain = mapCandidate[dropMain.text.toString()]!!
            val candidateVice = mapCandidate[dropVice.text.toString()]!!
            val office = mapOffice[dropOffice.text.toString()]!!;
            val text = ediPlateName.text.toString()

            val plate = controller.savePlate(
                mainCandidateId = candidateMain.id,
                viceCandidateId = candidateVice.id,
                officeId = office.id,
                plateName = text
            )
            val list = controller.getPlatesDto()


            runOnUiThread {
                list.forEach {
                    Log.i("teste_plate",it.plateName)
                }
            }
        }.start()
    }
    private fun fetchOffice(){
        Thread{

            mapOffice = mutableMapOf()
            controller.getOfficesExecutive().forEach {
                mapOffice[it.name] = it
            }
            runOnUiThread {
                if( mapOffice.keys.isNotEmpty())
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
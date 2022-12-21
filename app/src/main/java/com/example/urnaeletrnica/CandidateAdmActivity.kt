package com.example.urnaeletrnica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.example.urnaeletrnica.controllers.DataBankGeralController
import com.example.urnaeletrnica.model.entities.Office
import com.example.urnaeletrnica.model.entities.Party
import com.example.urnaeletrnica.model.entities.Voter

class CandidateAdmActivity : AppCompatActivity() {
    private lateinit var btnSave:Button
    private lateinit var dropParty:AutoCompleteTextView
    private lateinit var dropOffice:AutoCompleteTextView
    private lateinit var dropVoter:AutoCompleteTextView

    private lateinit var controller:DataBankGeralController

    private lateinit var mapParty: MutableMap<String, Party>
    private lateinit var mapOffice: MutableMap<String,Office>
    private lateinit var mapVoter: MutableMap<String,Voter>

    private lateinit var editCandidateNumber:EditText

    private var lengthLimit:Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate_adm)


        btnSave = findViewById(R.id.btn_save_candidate)

        dropParty = findViewById(R.id.auto_party)
        dropOffice = findViewById(R.id.auto_office)
        dropVoter = findViewById(R.id.auto_voter)

        editCandidateNumber = findViewById(R.id.edit_candidate_number)

        val app = application as App

        controller = DataBankGeralController(applicationContext,contentResolver,app.db)

        btnSave.setOnClickListener {

        }
        fetchParty()
        fetchOffice()
        fetchVoters()

        editCandidateNumber.addTextChangedListener(object :TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        dropOffice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!s.isNullOrEmpty() ){
                    val key = s.toString()
                    if(mapOffice.containsKey(key)){
                        // TODO: terminar esse caralho
                        val office = mapOffice[key]
                        if(office!!.isExecutive){
                            lengthLimit = 2
                            val party =   mapParty[dropParty.text.toString()]
                            editCandidateNumber.setText( party!!.number )
                        }else{
                            lengthLimit = office!!.numberLength
                        }


                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }
    private fun fetchParty(){
        Thread{
            mapParty = mutableMapOf()
            controller.getPartys().forEach {
                mapParty[it.initials] = it
            }
            runOnUiThread {
                if (mapParty.isNotEmpty())
                    dropParty.setText( mapParty.keys.first().toString())
                dropParty.setAdapter( ArrayAdapter(this@CandidateAdmActivity,android.R.layout.simple_list_item_1, mapParty.keys.toList() ))
            }
        }.start()

    }
    private fun fetchOffice(){
        Thread{
            mapOffice = mutableMapOf()
            controller.getOffices().forEach {
                mapOffice[it.name] = it
            }
            runOnUiThread {
                if(mapOffice.isNotEmpty())
                    dropOffice.setText(mapOffice.keys.first().toString())
                dropOffice.setAdapter(ArrayAdapter(this@CandidateAdmActivity,android.R.layout.simple_list_item_1,mapOffice.keys.toList()))
            }
        }.start()
    }
    private fun fetchVoters(){
        Thread{
            mapVoter = mutableMapOf()
            controller.getVoters().forEach {
                val firstName = it.name.split(" ").first().toString()
                mapVoter["$firstName - ${it.voterTitle}"] = it
            }
            runOnUiThread {
                if(mapVoter.isNotEmpty())
                    dropVoter.setText(mapVoter.keys.first().toString())
                dropVoter.setAdapter(ArrayAdapter(this@CandidateAdmActivity,android.R.layout.simple_list_item_1,mapVoter.keys.toList()))
            }
        }.start()
    }
}
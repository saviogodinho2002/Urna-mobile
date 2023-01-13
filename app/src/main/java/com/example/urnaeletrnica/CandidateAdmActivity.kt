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
import android.widget.Toast
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
    private lateinit var editCandidateNumberPrefix:EditText

    private var lengthLimit:Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate_adm)


        btnSave = findViewById(R.id.btn_save_candidate)

        dropParty = findViewById(R.id.auto_party)
        dropOffice = findViewById(R.id.auto_office)
        dropVoter = findViewById(R.id.auto_voter)

        editCandidateNumber = findViewById(R.id.edit_candidate_number)
        editCandidateNumberPrefix = findViewById(R.id.edit_candidate_number_prefix)

        val app = application as App

        controller = DataBankGeralController(applicationContext,contentResolver,app.db)

        btnSave.setOnClickListener {

        }
        fetchPartyAndOffice()

        fetchVoters()


        editCandidateNumber.addTextChangedListener(object :TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().length > lengthLimit!!-2){
                    editCandidateNumber.setText(s.toString().substring(0,lengthLimit!!-2))
                    editCandidateNumber.setSelection(lengthLimit!!-2)
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        dropParty.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

               editCandidateNumberPrefix.setText(mapParty[s.toString()]!!.number)
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

                    Log.i("teste","$key")
                    if(mapOffice.containsKey(key)){
                        // TODO: terminar esse caralho
                        val office = mapOffice[key]
                        if(office!!.isExecutive){
                            lengthLimit = 2

                            editCandidateNumber.setText( "" )
                            editCandidateNumber.isEnabled = false
                        }else{

                            editCandidateNumber.isEnabled = true

                            lengthLimit = office!!.numberLength

                            val editText =  editCandidateNumber.text.toString()
                            if(editText.length > lengthLimit!!-2){
                                val numberDigited = editText.substring(0,lengthLimit!!-2)
                                editCandidateNumber.setText(  "${numberDigited}")

                            }

                        }

                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        btnSave.setOnClickListener {
            saveCandidate()
        }
    }
    private fun fetchPartyAndOffice(){
        Thread{
            mapParty = mutableMapOf()
            controller.getPartys().forEach {
                mapParty[it.initials] = it
            }
            mapOffice = mutableMapOf()
            controller.getOffices().forEach {
                mapOffice[it.name] = it
            }
            runOnUiThread {

                if(mapOffice.isNotEmpty() && mapParty.isNotEmpty()) {
                    dropParty.setText( mapParty.keys.first().toString())
                    lengthLimit =   mapOffice[mapOffice.keys.first()]!!.numberLength

                    editCandidateNumberPrefix.setText( mapParty[mapParty.keys.first()]!!.number )

                    dropOffice.setText(mapOffice.keys.first().toString())

                }
                dropOffice.setAdapter(ArrayAdapter(this@CandidateAdmActivity,android.R.layout.simple_list_item_1,mapOffice.keys.toList()))
                dropParty.setAdapter( ArrayAdapter(this@CandidateAdmActivity,android.R.layout.simple_list_item_1, mapParty.keys.toList() ))

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
    private fun saveCandidate(){
        Thread{
            try {

                val party = mapParty[dropParty.text.toString()]
                val office = mapOffice[dropOffice.text.toString()]
                val voter = mapVoter[dropVoter.text.toString()]
                val number = "${editCandidateNumberPrefix.text.toString()}${editCandidateNumber.text.toString()}"
                controller.saveCandidate(partyId = party!!.id, officeId = office!!.id, voterId = voter!!.id, numberCandidate = number )
            }catch (e:Exception){
                runOnUiThread {

                    Toast.makeText(this@CandidateAdmActivity,e.message.toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}
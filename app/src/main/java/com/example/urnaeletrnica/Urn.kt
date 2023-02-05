package com.example.urnaeletrnica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.urnaeletrnica.controllers.DataBankGeralController
import com.example.urnaeletrnica.model.entities.Office

class Urn : AppCompatActivity() {
    private lateinit var controller: DataBankGeralController
    private lateinit var officesList:MutableList<Office>
    private lateinit var officeIterator: Iterator<Office>
    private lateinit var txtOfficeToVote:TextView
    private lateinit var currentOffice: Office

    private lateinit var btnCofirm:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_urn)
        supportActionBar?.hide()
        val app = application as App
        controller = DataBankGeralController(applicationContext,contentResolver,app.db)

        txtOfficeToVote = findViewById(R.id.txt_office_to_to_vote)

        btnCofirm = findViewById(R.id.btn_confirm)

        officesList = mutableListOf()

        fetchOffices()

        btnCofirm.setOnClickListener {
            getNextOffice()
        }
    }
    private fun fetchOffices(){
        Thread{
            officesList.addAll(controller.getOfficesIsNotExecutiveHasCandidate())
            officesList.addAll(controller.getOfficesExecutive())
            runOnUiThread {
                if(officesList.isNotEmpty()){

                    officeIterator = officesList.iterator()
                    getNextOffice()
                }
            }

        }.start()
    }
    private fun getNextOffice(){
        ///69420
        if(officeIterator.hasNext()){
            currentOffice = officeIterator.next()
            txtOfficeToVote.text =  currentOffice.name
        }else{
            ///TODO: ENCERRAR ELEICAO
        }
    }

}
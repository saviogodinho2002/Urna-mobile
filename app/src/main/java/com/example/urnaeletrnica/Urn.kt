package com.example.urnaeletrnica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.urnaeletrnica.controllers.DataBankGeralController
import com.example.urnaeletrnica.model.entities.Office

class Urn : AppCompatActivity() {
    private lateinit var controller: DataBankGeralController
    private lateinit var officesList:MutableList<Office>
    private lateinit var officeIterator: Iterator<Office>
    private lateinit var txtOfficeToVote:TextView
    private lateinit var txtNumberToVote:TextView
    private lateinit var currentOffice: Office

    private lateinit var btnCofirm:Button

    private lateinit var buttonsNumberList: MutableList<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_urn)
        supportActionBar?.hide()
        val app = application as App
        controller = DataBankGeralController(applicationContext,contentResolver,app.db)

        txtOfficeToVote = findViewById(R.id.txt_office_to_to_vote)

        btnCofirm = findViewById(R.id.btn_confirm)
        txtNumberToVote = findViewById(R.id.txt_digited_number)
        buttonsNumberList = mutableListOf(
            findViewById(R.id.btn_0),
            findViewById(R.id.btn_1),
            findViewById(R.id.btn_3),
            findViewById(R.id.btn_4),
            findViewById(R.id.btn_5),
            findViewById(R.id.btn_6),
            findViewById(R.id.btn_7),
            findViewById(R.id.btn_8),
            findViewById(R.id.btn_9),

            )
        buttonsNumberList.forEach { btn->
            btn.setOnClickListener {
                txtNumberToVote.text = "${ txtNumberToVote.text.toString() }${btn.text} "
            }
        }

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
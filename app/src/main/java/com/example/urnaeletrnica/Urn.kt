package com.example.urnaeletrnica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.example.urnaeletrnica.controllers.DataBankGeralController
import com.example.urnaeletrnica.model.entities.Office
import com.example.urnaeletrnica.model.entities.VotesElection
import com.example.urnaeletrnica.model.relationship.CandidateDto
import com.example.urnaeletrnica.model.relationship.PlateDto

class Urn : AppCompatActivity() {
    private lateinit var controller: DataBankGeralController
    private lateinit var officesList:MutableList<Office>
    private lateinit var officeIterator: Iterator<Office>
    private lateinit var txtOfficeToVote:TextView
    private lateinit var txtNumberToVote:TextView
    private lateinit var currentOffice: Office

    private lateinit var btnConfirm:Button
    private lateinit var btnCorrects:Button
    private lateinit var btnBlank:Button

    private lateinit var buttonsNumberList: MutableList<Button>
    private lateinit var txtCandidateName:TextView
    private lateinit var txtPartyName:TextView
    private lateinit var imgMain:ImageView
    private lateinit var imgVice:ImageView

    private var currentCandidateDto:CandidateDto? = null
    private var currentPlateDto:PlateDto? = null

    private lateinit var txtMainName:TextView
    private lateinit var txtViceName:TextView

    private lateinit var votesElectionList:MutableList<VotesElection>

    private var currentVoterId:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_urn)
        supportActionBar?.hide()
        val app = application as App
        controller = DataBankGeralController(applicationContext,contentResolver,app.db)

        txtOfficeToVote = findViewById(R.id.txt_office_to_to_vote)
        txtCandidateName = findViewById(R.id.name_current_candidate)
        txtPartyName = findViewById(R.id.party_current_candidate)
        txtMainName = findViewById(R.id.txt_main_name)
        txtViceName = findViewById(R.id.txt_vice_name)

        imgMain = findViewById(R.id.img_main)
        imgVice = findViewById(R.id.img_vice)

        votesElectionList = mutableListOf()

        btnConfirm = findViewById(R.id.btn_confirm)
        btnBlank = findViewById(R.id.btn_blank)
        txtNumberToVote = findViewById(R.id.txt_digited_number)
        buttonsNumberList = mutableListOf(
            findViewById(R.id.btn_0),
            findViewById(R.id.btn_1),
            findViewById(R.id.btn_2),
            findViewById(R.id.btn_3),
            findViewById(R.id.btn_4),
            findViewById(R.id.btn_5),
            findViewById(R.id.btn_6),
            findViewById(R.id.btn_7),
            findViewById(R.id.btn_8),
            findViewById(R.id.btn_9),

            )
        currentVoterId = intent.extras?.getInt("voter_id")!!
        btnCorrects = findViewById(R.id.btn_corrects)
        buttonsNumberList.forEach { btn->
            btn.setOnClickListener {

                 txtNumberToVote.text = "${txtNumberToVote.text.toString()}${btn.text}"
            }
        }

        officesList = mutableListOf()

        fetchOffices()

        btnConfirm.setOnClickListener {
            saveVote(false)
            getNextOffice()
        }
        btnBlank.setOnClickListener {
            saveVote(true)
            getNextOffice()
        }
        btnCorrects.setOnClickListener {
            val number = txtNumberToVote.text.toString()
            if(number.isNotEmpty())
                txtNumberToVote.text = number.substring(0, number.lastIndex)
        }
        txtNumberToVote.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(text.toString().length > currentOffice.numberLength){
                    txtNumberToVote.text = text.toString().substring(0, text.toString().lastIndex)
                }else if(text.toString().length == currentOffice.numberLength){
                    getCandidateOrPlateByNumber(text.toString());
                }else if( (currentCandidateDto != null || currentPlateDto != null) && text.toString().isNotEmpty()){

                    resetTextsAndImages()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }
    private fun resetTextsAndImages(){
        currentCandidateDto = null
        currentPlateDto = null
        txtCandidateName.text = ""
        txtPartyName.text = ""


        txtMainName.text = ""
        txtViceName.text = ""
        imgMain.setImageURI(null)
        imgVice.setImageURI(null)
    }
    private fun getCandidateOrPlateByNumber(number:String){
        resetTextsAndImages()

        Thread{
            try{
                if(currentOffice.isExecutive){

                    val plateDto = controller.getPlateDtoByPartyNumber(number, currentOffice.id)
                    if(plateDto is PlateDto){
                        currentPlateDto = plateDto
                        val candidateMain = controller.getVoterByCandidateId(currentPlateDto!!.mainId)
                        val candidateVice = controller.getVoterByCandidateId(currentPlateDto!!.viceId)
                        runOnUiThread {
                            setData(currentPlateDto!!.plateName,currentPlateDto!!.partyInitials,candidateMain.photoUri,candidateVice.photoUri,candidateMain.name,candidateVice.name)
                        }

                    }
                }else{
                    val candidateDto:CandidateDto = controller.getCandidateDtoByNumberAndOffice(number, currentOffice.id)
                    Log.e("teste",currentOffice.id.toString())
                    // val candidateDto = controller.getCandidateDtoByNumber(number)
                    if(candidateDto is CandidateDto) {
                        currentCandidateDto = candidateDto
                        runOnUiThread {
                            setData(currentCandidateDto!!.voterName,currentCandidateDto!!.partyInitials,currentCandidateDto!!.photoUri, null,null,null)

                        }

                    }
                }
            }catch (error:Exception){
//
            }

        }.start()

    }
    private fun setData(name:String, partyInitias:String, photoUriMain:String?,photoUriVice:String?,mainName:String?,viceName:String?){
        txtCandidateName.text = name
        txtPartyName.text = partyInitias

        mainName?.let {

            txtMainName.text = it
        }
        viceName?.let {

            txtViceName.text = it
        }
        photoUriMain?.let {
            imgMain.setImageURI(it.toUri())
        }
        photoUriVice?.let {
            imgVice.setImageURI(it.toUri())
        }
    }
    private fun fetchOffices(){
        Thread{
            officesList.addAll(controller.getOfficesIsNotExecutiveHasCandidate())
            officesList.addAll(controller.getOfficesExecutiveHasPlate())
            //officesList.addAll(controller.getOffices())
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
            resetTextsAndImages()
            txtNumberToVote.text = ""
            //txtNumberToVote. =currentOffice.numberLength
        }else{
            makeVotes()
        }
    }
    private fun makeVotes(){

        Thread{
            controller.saveVotesElections(votesElectionList)

            runOnUiThread {
                finish()
            }

        }.start()
    }
    private fun saveVote(blank:Boolean){

        votesElectionList.add(VotesElection(
            officeId = currentOffice.id,
           votedNumber =   if(blank) null else txtNumberToVote.text.toString(),
            voterId = currentVoterId
        ))
    }

}
package com.example.urnaeletrnica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.urnaeletrnica.controllers.DataBankGeralController
import com.example.urnaeletrnica.model.entities.Office
import com.example.urnaeletrnica.model.relationship.CandidateDto
import com.example.urnaeletrnica.model.relationship.PlateDto


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

    private lateinit var adapter: PlateAdmActivity.ListPlateAdapter
    private lateinit var recyclerPlate:RecyclerView
    private lateinit var plateDtoData:MutableList<PlateDto>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plate_adm)
        val app = application as App

        controller = DataBankGeralController(applicationContext,contentResolver,app.db)

        plateDtoData = mutableListOf()
        recyclerPlate = findViewById(R.id.recycler_plate)
        adapter = ListPlateAdapter(plateDtoData){id,item,view ->
            when(id){
                0->deletePlate(item!!)
            }
        }
        recyclerPlate.layoutManager = LinearLayoutManager(this@PlateAdmActivity)
        recyclerPlate.adapter = adapter;

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
        fetchPlate()
    }
    private fun deletePlate(plateDto: PlateDto){
        Thread{
            val plate = controller.getPlatesById(plateDto.id)
            controller.deletePlate(plate)
            val index = plateDtoData.indexOf(plateDto)
            plateDtoData.remove(plateDto)
            runOnUiThread {
                adapter.notifyItemRemoved(index)
            }
        }.start()

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
            plateDtoData.clear()
            plateDtoData.addAll(controller.getPlatesDto())


            runOnUiThread {
                adapter.notifyDataSetChanged()

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
    private fun fetchPlate(){
        Thread{
            plateDtoData.addAll(controller.getPlatesDto())
            runOnUiThread {
                adapter.notifyDataSetChanged()
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
    private inner class ListPlateAdapter(
        private val plateList:List<PlateDto>,
        private val actions:((Int, PlateDto?, View?)->Unit)
    ): RecyclerView.Adapter<ListPlateAdapter.ListPlateDtoViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPlateDtoViewHolder {
            val view = layoutInflater.inflate(R.layout.photo_three_text_layout,parent,false)
            return ListPlateDtoViewHolder(view)
        }

        override fun onBindViewHolder(currentViewHolder: ListPlateAdapter.ListPlateDtoViewHolder, position: Int) {
            val currentItem = plateList[position]
            currentViewHolder.bind(currentItem)
        }

        override fun getItemCount(): Int {
            return plateList.size
        }

        private inner class ListPlateDtoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            fun bind(item: PlateDto){

                val imgDelete = itemView.findViewById<ImageView>(R.id.img_icon_delet)

                val profileImg = itemView.findViewById<ImageView>(R.id.img_photo_profile)
                val name = itemView.findViewById<TextView>(R.id.txt_text_one)
                val partyName = itemView.findViewById<TextView>(R.id.txt_text_two)
                val number = itemView.findViewById<TextView>(R.id.txt_text_three)

                name.text = item.plateName
                partyName.text = item.partyInitials
                number.text = ""

                item.partyPhotoUrl?.let {
                    profileImg.setImageURI(it.toUri())
                }

                //name.text = item.voterName
                //partyName.text = item.zone.zoneNumber
                //candidateNumber.text = item.section.sectionNumber

                imgDelete.setOnClickListener {
                    actions.invoke(0,item,itemView)
                }

            }
        }

    }
}
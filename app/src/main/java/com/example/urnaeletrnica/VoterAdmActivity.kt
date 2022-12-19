package com.example.urnaeletrnica;

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.urnaeletrnica.controllers.DataBankGeralController
import com.example.urnaeletrnica.model.entities.Voter
import com.example.urnaeletrnica.model.relationship.SectionAndZone
import com.example.urnaeletrnica.model.relationship.VoterAndSection
import com.example.urnaeletrnica.model.relationship.ZoneSectionVoter

class VoterAdmActivity : AppCompatActivity() {
    private lateinit var controller: DataBankGeralController

    private lateinit var editVoterName:EditText
    private lateinit var editVoterTittle:EditText
    private lateinit var selectPhotoProfile:ImageView
    private lateinit var recyclerVoters:RecyclerView
    private lateinit var dropSections:AutoCompleteTextView
    private lateinit var voterData:MutableList<ZoneSectionVoter>
    private lateinit var btnSave:Button

    private lateinit var adapter: ListVoterAdapter
    private var imgUri: Uri? = null

    private lateinit var mapSectionAndZone: MutableMap<String, SectionAndZone>
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voter_adm)

        dropSections = findViewById(R.id.auto_section)
        recyclerVoters = findViewById(R.id.recycler_voter)
        editVoterName = findViewById(R.id.edit_voter_name)
        editVoterTittle = findViewById(R.id.edit_voter_voterTitle)
        selectPhotoProfile = findViewById(R.id.img_voter_photo)
        btnSave = findViewById(R.id.btn_save_voter)

        voterData = mutableListOf()
        adapter = ListVoterAdapter(voterData){id,item,view ->
            when(id){
                0 -> deleteVoter(item?.voter!!)
            }
        }
        recyclerVoters.layoutManager = LinearLayoutManager(this@VoterAdmActivity)
        recyclerVoters.adapter = adapter

        val app = application as App
        controller = DataBankGeralController(applicationContext,contentResolver,app.db)
        mapSectionAndZone = mutableMapOf()

        fetchSectionsOnDrop()

        btnSave.setOnClickListener {
            saveVoter()
        }
        fetchVoters()
    }
    private fun fetchVoters(){
        Thread{
            val response = controller.getSectionAndVoters()
            response.forEach { sectionAndVoters ->
                val zone = controller.getZoneById(sectionAndVoters.section!!.zoneId)
                sectionAndVoters.voters?.forEach { voter ->

                    voterData.add( ZoneSectionVoter(   zone, sectionAndVoters.section,voter  ) )
                }
            }
            runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }.start()
    }
    private fun formIsValid():Boolean{
        return (
                editVoterName.text.toString().trim().isNotEmpty()
                        &&
                editVoterTittle.text.toString().trim().isNotEmpty()
                        &&
                dropSections.text.toString().trim().isNotEmpty()
                        &&
                editVoterTittle.text.toString().trim().length == 12
                )
    }
    private fun saveVoter(){
        if(!formIsValid())
            return
        Thread{
            val key:String = dropSections.text.toString().trim()
            val section = mapSectionAndZone[key]!!.section!!
            val zone =  mapSectionAndZone[key]!!.zone!!
            val voter = controller.saveVoter(
                editVoterName.text.toString().trim(),
                editVoterTittle.text.toString().trim(),
                section.id,
                imgUri
            )
            voterData.add( ZoneSectionVoter(zone,section, voter) )
            runOnUiThread {
                adapter.notifyItemInserted(voterData.lastIndex)
            }
        }.start()
    }
    private fun deleteVoter(voter:Voter){
        Thread{
           // controller.deleteVoter(voter)
        }.start()
    }
    private  fun fetchSectionsOnDrop(){
        Thread{
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
    private inner class ListVoterAdapter(
        private val voterList:List<ZoneSectionVoter>,
        private val actions:((Int, ZoneSectionVoter?, View?)->Unit)
    ): RecyclerView.Adapter<ListVoterAdapter.ListSectionViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSectionViewHolder {
            val view = layoutInflater.inflate(R.layout.photo_three_text_layout,parent,false)
            return ListSectionViewHolder(view)
        }

        override fun onBindViewHolder(currentViewHolder: ListVoterAdapter.ListSectionViewHolder, position: Int) {
            val currentItem = voterList[position]
            currentViewHolder.bind(currentItem)
        }

        override fun getItemCount(): Int {
            return voterList.size
        }

        private inner class ListSectionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            fun bind(item: ZoneSectionVoter){

                val imgDelete = itemView.findViewById<ImageView>(R.id.img_icon_delet)
                val name = itemView.findViewById<TextView>(R.id.txt_text_one)
                val zone = itemView.findViewById<TextView>(R.id.txt_text_two)
                val section = itemView.findViewById<TextView>(R.id.txt_text_three)

                name.text = item.voter.name
                zone.text = item.zone.zoneNumber
                section.text = item.section.sectionNumber

                imgDelete.setOnClickListener {
                    actions.invoke(0,item,itemView)
                }

            }
        }

    }
}
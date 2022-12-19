package com.example.urnaeletrnica;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.urnaeletrnica.controllers.DataBankGeralController
import com.example.urnaeletrnica.model.relationship.SectionAndZone
import com.example.urnaeletrnica.model.relationship.VoterAndSection

class VoterAdmActivity : AppCompatActivity() {
    private lateinit var controller: DataBankGeralController

    private lateinit var editVoterName:EditText
    private lateinit var editVoterTittle:EditText
    private lateinit var selectPhotoProfile:ImageView
    private lateinit var dropSections:AutoCompleteTextView

    private lateinit var adapter: ListVoterAdapter

    private lateinit var mapSectionAndZone: MutableMap<String, SectionAndZone>
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voter_adm)

        dropSections = findViewById(R.id.auto_section)


        val app = application as App
        controller = DataBankGeralController(applicationContext,contentResolver,app.db)
        mapSectionAndZone = mutableMapOf()

        fetchSectionsOnDrop()
    }

    private  fun fetchSectionsOnDrop(){
        Thread{
            val items = controller.getZoneAndSections()   // daoZone.getZonesNumber()
            items.forEach { zoneAndSections ->
                zoneAndSections.getSectionAndZone().forEach {sectionAndZone->
                    val key = sectionAndZone.zone!!.zoneNumber + " - " + sectionAndZone.section!!.sectionNumber
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
        private val voterList:List<VoterAndSection>,
        private val actions:((Int, VoterAndSection?, View?)->Unit)
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
            fun bind(item: VoterAndSection){

                val imgDelete = itemView.findViewById<ImageView>(R.id.img_icon_delet)

                imgDelete.setOnClickListener {
                    actions.invoke(0,item,itemView)
                }

            }
        }

    }
}
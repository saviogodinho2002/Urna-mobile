package com.example.urnaeletrnica;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView;
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.urnaeletrnica.controllers.DataBankSectionController
import com.example.urnaeletrnica.controllers.DataBankZoneController
import com.example.urnaeletrnica.model.entities.Section

class SectionAdmActivity : AppCompatActivity() {
    private lateinit var dropZoneNumbers:AutoCompleteTextView;
    private lateinit var zoneController:DataBankZoneController
    private lateinit var sectionController:DataBankSectionController
    private lateinit var adapter: ListSectionAdapter
    private lateinit var sectionData: MutableList<Section>
    private lateinit var recyclerViewSection: RecyclerView
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_adm);

        val app = application as App

        val daoZone = app.db.ZoneDao()
        val daoSection = app.db.SectionDao()

        sectionData = mutableListOf()
        adapter = ListSectionAdapter(sectionData){id,item,view->
            when(id){
                0 -> deleteSection(item!!)
            }
        }

        zoneController = DataBankZoneController(applicationContext,contentResolver,daoZone)
        sectionController = DataBankSectionController(applicationContext,contentResolver,daoSection)

        dropZoneNumbers = findViewById(R.id.auto_section)

        recyclerViewSection = findViewById(R.id.recycler_section)
        recyclerViewSection.layoutManager = LinearLayoutManager(this)
        recyclerViewSection.adapter = adapter

        fetchZonesOnDrop()


    }
    private  fun fetchZonesOnDrop(){
        Thread{
            val items = zoneController.getZoneNumbers()   // daoZone.getZonesNumber()

            runOnUiThread{

                val adapterDrop = ArrayAdapter(this,android.R.layout.simple_list_item_1, items )
                if (items.isNotEmpty())
                    dropZoneNumbers.setText(items.first())
                dropZoneNumbers.setAdapter(adapterDrop)


            }
        }.start()
    }
    private fun deleteSection(section: Section){

    }

    private inner class ListSectionAdapter(
        private val sectionList:List<Section>,
        private val actions:((Int, Section?, View?)->Unit)
    ): RecyclerView.Adapter<ListSectionAdapter.ListSectionViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSectionViewHolder {
            val view = layoutInflater.inflate(R.layout.two_text_layout,parent,false)
            return ListSectionViewHolder(view)
        }

        override fun onBindViewHolder(currentViewHolder: ListSectionAdapter.ListSectionViewHolder, position: Int) {
            val currentItem = sectionList[position]
            currentViewHolder.bind(currentItem)
        }

        override fun getItemCount(): Int {
            return sectionList.size
        }

        private inner class ListSectionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            fun bind(item: Section){
                val txtSectionNumber = itemView.findViewById<TextView>(R.id.txt_item_name)
                val txtZoneNumber = itemView.findViewById<TextView>(R.id.txt_item_detail)
                val imgDelete = itemView.findViewById<ImageView>(R.id.img_icon_delet)

                imgDelete.setOnClickListener {
                    actions.invoke(0,item,itemView)
                }

            }
        }

    }


}
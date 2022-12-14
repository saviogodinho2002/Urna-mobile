package com.example.urnaeletrnica;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.urnaeletrnica.controllers.DataBankGeralController
import com.example.urnaeletrnica.model.relationship.SectionAndZone
import com.example.urnaeletrnica.model.relationship.ZoneAndSections

class SectionAdmActivity : AppCompatActivity() {
    private lateinit var dropZoneNumbers:AutoCompleteTextView;

    private lateinit var adapter: ListSectionAdapter
    private lateinit var sectionAndZoneList: MutableList<SectionAndZone>
    private lateinit var recyclerViewSection: RecyclerView
    private lateinit var btnSaveSection:Button
    private lateinit var cxSectionNumber:EditText

    private lateinit var controller: DataBankGeralController
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_adm);

        val app = application as App


        sectionAndZoneList = mutableListOf()

        adapter = ListSectionAdapter(sectionAndZoneList){ id, item, view->
            when(id){
                0 -> deleteSection(item!!)
            }
        }

        controller = DataBankGeralController(applicationContext,contentResolver,app.db)

        dropZoneNumbers = findViewById(R.id.auto_section)

        recyclerViewSection = findViewById(R.id.recycler_section)
        recyclerViewSection.layoutManager = LinearLayoutManager(this)
        recyclerViewSection.adapter = adapter

        fetchZonesOnDrop()

        cxSectionNumber = findViewById(R.id.edit_section_number)
        btnSaveSection = findViewById(R.id.btn_save_section)
        btnSaveSection.setOnClickListener {
            saveSection()
        }
        fetchSections()
    }
    private fun fetchSections(){
        Thread{
           controller.getSectionAndZone()
               .forEach {
                if(it.section != null ){
                    val zone = it.zone
                    it.section.forEach { currentSection->
                        sectionAndZoneList.add( SectionAndZone(zone,currentSection))
                    }
                }

            }
            runOnUiThread{

                adapter.notifyDataSetChanged()

            }
        }.start()
    }

    private  fun fetchZonesOnDrop(){
        Thread{
            val items = controller.getZoneNumbers()   // daoZone.getZonesNumber()

            runOnUiThread{

                val adapterDrop = ArrayAdapter(this,android.R.layout.simple_list_item_1, items )
                if (items.isNotEmpty())
                    dropZoneNumbers.setText(items.first())
                dropZoneNumbers.setAdapter(adapterDrop)


            }
        }.start()
    }
    private fun deleteSection(sectionAndZone: SectionAndZone){
        Thread{
            val old = controller.deleteSection(sectionAndZone.section!!)
            runOnUiThread {
                val index = sectionAndZoneList.indexOf(sectionAndZone)
                adapter.notifyItemRemoved(index)
                sectionAndZoneList.remove(sectionAndZone)
            }
        }.start()

    }
    private fun formIsValid():Boolean{
        return (dropZoneNumbers.text.toString().trim().isNotEmpty() && cxSectionNumber.text.toString().trim().isNotEmpty())

    }
    private fun saveSection(){
        if(!formIsValid()){
            Toast.makeText(this@SectionAdmActivity,getString(R.string.forms_invalid),Toast.LENGTH_SHORT).show()
            return
        }
        Thread{
            val sectionAndZone = controller.saveSection(cxSectionNumber.text.toString().trim(),dropZoneNumbers.text.toString().trim())
            runOnUiThread {
                sectionAndZoneList.add(sectionAndZone)
                adapter.notifyItemInserted(sectionAndZoneList.lastIndex)
            }
        }.start()
    }

    private inner class ListSectionAdapter(
        private val sectionList:List<SectionAndZone>,
        private val actions:((Int, SectionAndZone?, View?)->Unit)
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
            fun bind(item: SectionAndZone){
                val txtSectionNumber = itemView.findViewById<TextView>(R.id.txt_item_name)
                val txtZoneNumber = itemView.findViewById<TextView>(R.id.txt_item_detail)
                val imgDelete = itemView.findViewById<ImageView>(R.id.img_icon_delet)

                txtSectionNumber.text = item.section!!.sectionNumber
                txtZoneNumber.text = item.zone!!.zoneNumber

                imgDelete.setOnClickListener {
                    actions.invoke(0,item,itemView)
                }

            }
        }

    }


}
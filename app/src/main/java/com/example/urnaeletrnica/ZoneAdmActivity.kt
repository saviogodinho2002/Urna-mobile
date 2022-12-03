package com.example.urnaeletrnica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.urnaeletrnica.controllers.DataBankZoneController
import com.example.urnaeletrnica.model.entities.Party
import com.example.urnaeletrnica.model.entities.Zone
import java.lang.Exception

class ZoneAdmActivity : AppCompatActivity() {
    private lateinit var zoneController: DataBankZoneController
    private lateinit var recyclerViewZone: RecyclerView
    private lateinit var zoneData: MutableList<Zone>
    private lateinit var adapter:ListZoneAdapter
    private lateinit var editZoneName:EditText
    private lateinit var editZoneNumber:EditText
    private lateinit var buttonSave:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zone_adm)

        editZoneName = findViewById(R.id.edit_zone_name)
        editZoneNumber = findViewById(R.id.edit_zone_number)
        buttonSave = findViewById(R.id.btn_save_zone)

        val app = application as App
        val dao = app.db.ZoneDao()
        zoneController = DataBankZoneController(applicationContext,contentResolver,dao)

        zoneData = mutableListOf()
        adapter = ListZoneAdapter(zoneData){id,item,itemView ->
            when(id){
                0 -> deletZone(item!!)
            }
        }
        recyclerViewZone = findViewById(R.id.recycler_zone)
        recyclerViewZone.layoutManager = LinearLayoutManager(this)
        recyclerViewZone.adapter = adapter

        fetchData()

       buttonSave.setOnClickListener {
           insertZone()
       }

    }
    private fun formIsValid():Boolean{
        return (
                    editZoneName.text.toString().trim().isEmpty()
                            ||
                    editZoneNumber.text.toString().trim().isEmpty()

                )
    }
    private fun resetForm(){
        editZoneName.setText("")
        editZoneNumber.setText("")
    }
    private fun insertZone(){
        if(formIsValid()){
            Toast.makeText(this,getString( R.string.forms_invalid),Toast.LENGTH_SHORT).show()
        }

        Thread{
            try {
                val zone = zoneController.saveZone(
                    nameZone =  editZoneName.text.toString().trim(),
                    number = editZoneNumber.text.toString().trim()
                )
                zoneData.add(zone)
                runOnUiThread{
                    adapter.notifyItemInserted(zoneData.size-1)
                    resetForm()
                }
            }catch (error:Exception){
                runOnUiThread {
                    Toast.makeText(this@ZoneAdmActivity,error.message,Toast.LENGTH_SHORT).show()
                }

            }
        }.start()
    }
    private fun deletZone(zone: Zone){
        Thread{
            val index = zoneData.indexOf(zone)
            zoneData.remove(zone)
            zoneController.deleteZone(zone)
            runOnUiThread {
                adapter.notifyItemRemoved(index)
            }
        }.start()
    }
    private fun fetchData(){
        Thread{
            val response = zoneController.getZones()
            zoneData.addAll(response)
            runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }.start()
    }
    private inner class ListZoneAdapter(
        private val zoneList:List<Zone>,
        private val actions:((Int, Zone?, View?)->Unit)
    ):RecyclerView.Adapter<ListZoneAdapter.ListZoneViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListZoneViewHolder {
            val view = layoutInflater.inflate(R.layout.zone_layout,parent,false)
            return ListZoneViewHolder(view)
        }

        override fun onBindViewHolder(currentViewHolder: ListZoneAdapter.ListZoneViewHolder, position: Int) {
            val currentItem = zoneList[position]
            currentViewHolder.bind(currentItem)
        }

        override fun getItemCount(): Int {
            return zoneList.size
        }

        private inner class ListZoneViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
            fun bind(item: Zone){
                val txtZoneName = itemView.findViewById<TextView>(R.id.txt_zone_name)
                val txtZoneNumber = itemView.findViewById<TextView>(R.id.txt_zone_num)
                val imgDelete = itemView.findViewById<ImageView>(R.id.img_icon_delet)

                txtZoneName.text = item.zoneName
                txtZoneNumber.text = item.zoneNumber

                imgDelete.setOnClickListener {
                    actions.invoke(0,item,itemView)
                }

            }
        }

    }

}
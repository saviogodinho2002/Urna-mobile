package com.example.urnaeletrnica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.urnaeletrnica.model.entities.Office

class OfficeAdm : AppCompatActivity() {


    private lateinit var editOfficeName: EditText;
    private lateinit var editOfficeMaxNumber: EditText;

    private lateinit var btnSaveUpdate: Button;

    private lateinit var adapter: ListOfficeAdapter;
    private lateinit var recyclerView: RecyclerView;
    private lateinit var officeData:MutableList<Office>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_office_adm)

        editOfficeName = findViewById(R.id.edit_office_name);
        editOfficeMaxNumber = findViewById(R.id.edit_office_initials);
        btnSaveUpdate = findViewById(R.id.btn_office_save);

        officeData = mutableListOf();
        adapter = ListOfficeAdapter(officeData){id, office, view ->

        }

        recyclerView = findViewById(R.id.recycler_office);
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter;


        //fetch data
        Thread{
            val app = application as App
            val dao = app.db.OfficeDao()

            val response = dao.getOffices()

            runOnUiThread {
                officeData.addAll(response)
                adapter.notifyDataSetChanged()
            }

        }.start()


        btnSaveUpdate.setOnClickListener {

        }


    }

    private inner class ListOfficeAdapter(
        private val officeList:List<Office>,
        private val actions:((Int,Office,View)->Unit)
    ):RecyclerView.Adapter<ListOfficeAdapter.ListOfficeViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ListOfficeAdapter.ListOfficeViewHolder {
            val view = layoutInflater.inflate(R.layout.office_layout,parent,false)
            return ListOfficeViewHolder(view)
        }

        override fun onBindViewHolder(currentViewHolder: ListOfficeAdapter.ListOfficeViewHolder, position: Int ) {
            val currentItem = officeList[position]
            currentViewHolder.bind(currentItem)
        }

        override fun getItemCount(): Int {
            return officeList.size
        }
        private inner class ListOfficeViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
            fun bind(item: Office){
                val txtName = itemView.findViewById<TextView>(R.id.txt_office_name);
                val txtNumMax = itemView.findViewById<TextView>(R.id.txt_office_nummax);

                txtName.text = item.name;
                txtNumMax.text = item.numberQuant.toString();
            }
        }

    }


}
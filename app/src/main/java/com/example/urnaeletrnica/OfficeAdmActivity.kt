package com.example.urnaeletrnica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.urnaeletrnica.controllers.DataBankOfficeController
import com.example.urnaeletrnica.model.entities.Office

class OfficeAdmActivity : AppCompatActivity() {


    private lateinit var editOfficeName: EditText;
    private lateinit var editOfficeMaxNumber: EditText;

    private lateinit var btnSaveUpdate: Button;
    private lateinit var switchExecutive:SwitchCompat;

    private lateinit var adapter: ListOfficeAdapter;
    private lateinit var recyclerView: RecyclerView;
    private lateinit var officeData:MutableList<Office>

    private lateinit var officeController: DataBankOfficeController;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_office_adm)

        editOfficeName = findViewById(R.id.edit_office_name);
        editOfficeMaxNumber = findViewById(R.id.edit_office_initials);
        btnSaveUpdate = findViewById(R.id.btn_office_save);
        switchExecutive = findViewById(R.id.switch_executive)

        officeData = mutableListOf();
        adapter = ListOfficeAdapter(officeData){id, office, view ->
            when(id){
                0 ->deleteOffice(office);
            }
        }

        recyclerView = findViewById(R.id.recycler_office);
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter;

        val app = application as App
        val dao = app.db.OfficeDao()
        officeController = DataBankOfficeController(applicationContext,contentResolver,dao);
        fetchData()

        btnSaveUpdate.setOnClickListener {
            saveOffice();
        }
        switchExecutive.setOnCheckedChangeListener { compoundButton, b ->
            Log.i("teste",""+b)
            editOfficeMaxNumber.isEnabled = !b;
        }


    }
    private fun fetchData(){
        Thread{

            val response = officeController.getOffices()

            runOnUiThread {
                officeData.addAll(response)
                adapter.notifyDataSetChanged()
            }

        }.start()


    }
    private fun deleteOffice(office: Office){
        Thread{
            officeController.deleteOffice(office)

            runOnUiThread {
                val index = officeData.indexOf(office)
                officeData.remove(office)
                adapter.notifyItemRemoved(index)
            }

        }.start()
    }
    private fun saveOffice(){
        if(!formIsValid()){
            return;
        }
        Thread{

            val number =if(switchExecutive.isChecked)
                            2
                        else
                            Integer.parseInt(editOfficeMaxNumber.text.toString());

            val office = officeController.saveOffice(editOfficeName.text.toString().trim(),
                                 number,
                                switchExecutive.isChecked)
            officeData.add(office)
            runOnUiThread {
                adapter.notifyItemInserted(officeData.size-1)
            }

        }.start()



    }
    private fun formIsValid():Boolean{
        return (editOfficeName.text.toString().trim().isNotEmpty()
                && (switchExecutive.isChecked || editOfficeMaxNumber.text.toString().trim().isNotEmpty()   ))
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
                val iconDelete = itemView.findViewById<ImageView>(R.id.img_icon_delet)

                txtName.text = item.name;
                txtNumMax.text = item.numberLength.toString();

                iconDelete.setOnClickListener {
                    actions.invoke(0,item,itemView)
                }
            }
        }

    }


}
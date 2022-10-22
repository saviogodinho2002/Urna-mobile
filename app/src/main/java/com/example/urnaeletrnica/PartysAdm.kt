package com.example.urnaeletrnica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.urnaeletrnica.model.entities.Candidate
import com.example.urnaeletrnica.model.entities.Party

class PartysAdm : AppCompatActivity() {

    private lateinit var editPartyName:EditText;
    private lateinit var editPartyInitials:EditText;
    private lateinit var editPartyNumber:EditText;
    private lateinit var btnSave:Button;
    private lateinit var btnUpdate:Button;
    private lateinit var recyclerView: RecyclerView;

    private lateinit var partyData:MutableList<Party>;
    private lateinit var adapter:ListPartyAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partys_adm)

        editPartyName = findViewById(R.id.edit_party_name);
        editPartyInitials = findViewById(R.id.edit_party_initials);
        editPartyNumber = findViewById(R.id.edit_party_number);

        btnSave = findViewById(R.id.btn_party_save);
        btnUpdate = findViewById(R.id.btn_party_update);

        recyclerView = findViewById(R.id.recycler_partys);


        partyData = mutableListOf<Party>()
        adapter = ListPartyAdapter(partyData);

        recyclerView.layoutManager = LinearLayoutManager(this);
        recyclerView.adapter = adapter;

        Thread{
            val app = application as App;
            val dao = app.db.PartyDao();

            val response = dao.getPartRegisters()

            runOnUiThread {
                partyData.addAll(response)
                adapter.notifyDataSetChanged()
            }

        }.start()

        btnSave.setOnClickListener {
            saveParty()
        }


    }

    private fun saveParty(){
        if(editPartyName.text.toString().isEmpty() ||
            editPartyInitials.text.toString().isEmpty() ||
            editPartyNumber.text.toString().isEmpty())
            return;

        val party = Party(
            name = editPartyName.text.toString(),
            logoPhoto = null,
            initials =  editPartyInitials.text.toString(),
            timeStamp = System.currentTimeMillis());
        Thread{
            val app = application as App;
            val dao = app.db.PartyDao()

            partyData.add(party)
            dao.insertParty(party)

            runOnUiThread{
                adapter.notifyDataSetChanged()
            }

        }.start()
    }

    private fun deletParty(party:Party){
        Thread{
            val app = application as App;
            val dao = app.db.PartyDao()

            partyData.remove(party);
            dao.deleteParty(party);

            runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }.start()
    }

    private inner class ListPartyAdapter(private val partyList:List<Party>):RecyclerView.Adapter<ListPartyAdapter.ListPartyViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPartyAdapter.ListPartyViewHolder {
            val view = layoutInflater.inflate(R.layout.party_layout,parent,false)
            return ListPartyViewHolder(view);
        }

        override fun onBindViewHolder(currentViewHolder: ListPartyAdapter.ListPartyViewHolder, position: Int) {
            val currentItem = partyList[position];
            currentViewHolder.bind(currentItem);
        }

        override fun getItemCount(): Int {
            return partyList.size
        }



        private inner class ListPartyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
            fun bind(item:Party){

                val txtName = itemView.findViewById<TextView>(R.id.txt_party_name)
                val txtInitial = itemView.findViewById<TextView>(R.id.txt_party_initial);
                val imgPhoto = itemView.findViewById<ImageView>(R.id.img_logo_party);

                val imgDelet = itemView.findViewById<ImageView>(R.id.img_icon_delet);


                txtName.text = item.name;
                txtInitial.text = item.initials;

                imgDelet.setOnClickListener {
                    Log.i("teste","clickdelete")
                    deletParty(item);
                }

            }
        }

    }



}
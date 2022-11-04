package com.example.urnaeletrnica

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.urnaeletrnica.controllers.DataBankPartyController
import com.example.urnaeletrnica.model.entities.Party
import com.example.urnaeletrnica.utils.InternalPhotosController
import java.io.File
import java.io.FileOutputStream
import java.util.*

class PartysAdm : AppCompatActivity() {

    private lateinit var editPartyName:EditText
    private lateinit var editPartyInitials:EditText
    private lateinit var editPartyNumber:EditText
    private lateinit var btnSaveOrUpdate:Button

    private lateinit var recyclerView: RecyclerView
    private lateinit var imgToParty:ImageView

    private lateinit var partyData:MutableList<Party>
    private lateinit var adapter:ListPartyAdapter

    private var imgUri: Uri? = null
    private var partyOnFocus:Party? = null
    private var partyOnFocusView:ConstraintLayout? = null
    private val directory = "party_photos";
    private lateinit var partyController:DataBankPartyController;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partys_adm)

        editPartyName = findViewById(R.id.edit_party_name)
        editPartyInitials = findViewById(R.id.edit_party_initials)
        editPartyNumber = findViewById(R.id.edit_party_number)

        btnSaveOrUpdate = findViewById(R.id.btn_party_save)


        imgToParty = findViewById(R.id.img_party_photo)

        recyclerView = findViewById(R.id.recycler_partys)


        partyData = mutableListOf<Party>()
        adapter = ListPartyAdapter(partyData){id,view,party->
            when(id){
                0 -> markParty(view!!,party!!)
                1 -> deleteParty(party!!)
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        val app = application as App
        val dao = app.db.PartyDao()
        partyController = DataBankPartyController(applicationContext,contentResolver,dao)

         fetchData()

        btnSaveOrUpdate.setOnClickListener {
            if (partyOnFocus == null)
                saveParty()
            else
                updateParty()
            btnSaveOrUpdate.setText(R.string.btn_save)
        }
        imgToParty.setOnClickListener {
            selectPhoto()
        }

    }
    private fun fetchData(){
        Thread{
        val response = partyController.getPartys()

        runOnUiThread {
            partyData.addAll(response)
            adapter.notifyDataSetChanged()
        }

        }.start()
    }
    private fun resetPartyFocus(){
        partyOnFocus = null
        partyOnFocusView = null
    }
    private fun markParty(layout: View,party: Party){
        partyOnFocusView?.setBackgroundColor(ContextCompat.getColor(this,android.R.color.transparent))
        if(partyOnFocus == party){
            resetPartyFocus()
            btnSaveOrUpdate.setText(R.string.btn_save)
            resetForm()
            return
        }
        insertDataInFom(party)
        btnSaveOrUpdate.setText(R.string.btn_update)
        partyOnFocusView = layout as ConstraintLayout
        partyOnFocus = party
        layout.setBackgroundColor(ContextCompat.getColor(this,android.R.color.holo_blue_dark) )
    }


    private fun resetForm(){
        editPartyName.setText("")
        editPartyInitials.setText("")
        editPartyNumber.setText("")
        imgToParty.setImageResource(R.drawable.ic_add_photo)
    }


    private fun formIsValid():Boolean{
        return (editPartyName.text.toString().trim().isNotEmpty() &&
            editPartyInitials.text.toString().trim().isNotEmpty() &&
            editPartyNumber.text.toString().trim().isNotEmpty())

    }
    private fun updateParty(){
        if(!formIsValid() && partyOnFocus == null)
            return
        var imgDirectory:String? = null
        Thread{

            if(imgUri != null){
                imgDirectory = InternalPhotosController.saveAndGetDirPhoto( applicationContext ,contentResolver,directory,imgUri!! )
            }else if(partyOnFocus!!.logoPhoto != null){
                imgDirectory = partyOnFocus!!.logoPhoto
            }
            val index =  partyData.indexOf(partyOnFocus)
            val party =
                    partyController.updateParty(
                        partyOnFocus!!,
                        imgUri,
                        editPartyName.text.toString().trim(),
                        editPartyInitials.text.toString().trim(),
                        editPartyNumber.text.toString().trim()
                    )
            
            partyData[partyData.indexOf(partyOnFocus!!)] = party
            partyOnFocus = null
            partyOnFocusView!!.setBackgroundColor(resources.getColor(android.R.color.transparent))
            partyOnFocusView = null
            imgUri = null

            runOnUiThread{
                adapter.notifyItemChanged(index)
                resetForm()
            }

        }.start()

    }
    private fun saveParty(){
        if(!formIsValid())
            return

        Thread{

            val party =
                        partyController.saveParty(
                            imgUri,
                            editPartyName.text.toString().trim(),
                            editPartyInitials.text.toString().trim(),editPartyNumber.text.toString().trim()
                        )

            partyData.add(party)
            imgUri = null;

            runOnUiThread{
                adapter.notifyItemInserted(partyData.size-1)
                resetForm()
            }

        }.start()
    }

    private fun deleteParty(party:Party){
        Thread{
            partyController.deleteParty(party);

            runOnUiThread {
                val index = partyData.indexOf(party)
                partyData.remove(party)
                adapter.notifyItemRemoved(index)
            }

        }.start()
    }

    private fun insertDataInFom(party: Party){
        editPartyName.setText(party.name)
        editPartyInitials.setText(party.initials)
        editPartyNumber.setText(party.number)

        imgToParty.setImageURI(party.logoPhoto?.toUri())
    }

    private fun selectPhoto(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        selectPhotoLauncher.launch(intent)

    }
    private var selectPhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        if (result.resultCode == Activity.RESULT_OK) {
            imgUri = result.data?.data
            imgToParty.setImageURI(imgUri)
        }
    }

    private inner class ListPartyAdapter(
        private val partyList:List<Party>,
        private val actions:((Int,View?,Party?)->Unit)
    ):RecyclerView.Adapter<ListPartyAdapter.ListPartyViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPartyAdapter.ListPartyViewHolder {
            val view = layoutInflater.inflate(R.layout.party_layout,parent,false)
            return ListPartyViewHolder(view)
        }

        override fun onBindViewHolder(currentViewHolder: ListPartyAdapter.ListPartyViewHolder, position: Int) {
            val currentItem = partyList[position]
            currentViewHolder.bind(currentItem)
        }

        override fun getItemCount(): Int {
            return partyList.size
        }
        
        private inner class ListPartyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
            fun bind(item:Party){

                val txtName = itemView.findViewById<TextView>(R.id.txt_party_name)
                val txtInitial = itemView.findViewById<TextView>(R.id.txt_party_initial)
                val txtNumber = itemView.findViewById<TextView>(R.id.txt_party_number)

                val imgPhoto = itemView.findViewById<ImageView>(R.id.img_logo_party)

                val imgDelet = itemView.findViewById<ImageView>(R.id.img_icon_delet)

                val layoutThisParty = itemView.findViewById<ConstraintLayout>(R.id.cl_party_item)
                if(item.logoPhoto != null){
                    imgPhoto.setImageURI(item.logoPhoto.toUri())
                }


                txtName.text = item.name
                txtInitial.text = item.initials
                txtNumber.text = item.number

                layoutThisParty.setOnClickListener {

                    actions.invoke(0, layoutThisParty, item)

                }

                imgDelet.setOnClickListener {
                    Log.i("teste","clickdelete")
                    actions.invoke(1,null,item)

                }

            }
        }

    }



}
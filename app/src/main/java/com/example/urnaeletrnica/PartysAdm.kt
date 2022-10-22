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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.urnaeletrnica.model.entities.Party
import java.io.File
import java.io.FileOutputStream
import java.util.*

class PartysAdm : AppCompatActivity() {

    private lateinit var editPartyName:EditText;
    private lateinit var editPartyInitials:EditText;
    private lateinit var editPartyNumber:EditText;
    private lateinit var btnSave:Button;
    private lateinit var btnUpdate:Button;
    private lateinit var recyclerView: RecyclerView;
    private lateinit var imgToParty:ImageView;

    private lateinit var partyData:MutableList<Party>;
    private lateinit var adapter:ListPartyAdapter;

    private var imgUri: Uri? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partys_adm)

        editPartyName = findViewById(R.id.edit_party_name);
        editPartyInitials = findViewById(R.id.edit_party_initials);
        editPartyNumber = findViewById(R.id.edit_party_number);

        btnSave = findViewById(R.id.btn_party_save);
        btnUpdate = findViewById(R.id.btn_party_update);

        imgToParty = findViewById(R.id.img_party_photo);

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
        imgToParty.setOnClickListener {
            selectPhoto()
        }


    }
    private fun resetForm(){
        editPartyName.setText("");
        editPartyInitials.setText("");
        editPartyNumber.setText("");
        imgToParty.setImageResource(R.drawable.ic_add_photo);
    }
    private fun removePhotoFile(photo:String){
        val file = File(photo);
         file.delete()
    }
    private fun saveAndGetDirPhoto(uri:Uri):String?{
        try {
            val cw = ContextWrapper(applicationContext);
            val directory = cw.getDir("party_photos",Context.MODE_PRIVATE);
            val path = File(directory,"${UUID.randomUUID().toString()}.jpg");

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imgUri);
            val out = FileOutputStream(path);

            bitmap.compress( Bitmap.CompressFormat.PNG,100,out);
            out.close();
            return path.absolutePath;

        }catch (error:Exception){

        }
        return  null;
    }

    private fun saveParty(){
        if(editPartyName.text.toString().isEmpty() ||
            editPartyInitials.text.toString().isEmpty() ||
            editPartyNumber.text.toString().isEmpty())
            return;
        var imgDirectory:String? = null;
        if(imgUri != null){
            imgDirectory = saveAndGetDirPhoto(imgUri!!);
        }
        val party = Party(
            name = editPartyName.text.toString().trim(),
            logoPhoto = imgDirectory,
            initials =  editPartyInitials.text.toString().trim(),
            number = editPartyNumber.text.toString().trim(),
            timeStamp = System.currentTimeMillis());
        Thread{
            val app = application as App;
            val dao = app.db.PartyDao()

            partyData.add(party)
            dao.insertParty(party)

            runOnUiThread{
                adapter.notifyDataSetChanged()
                resetForm();
            }

        }.start()
    }

    private fun deletParty(party:Party){
        Thread{
            val app = application as App;
            val dao = app.db.PartyDao()

            if (party.logoPhoto != null)
                removePhotoFile(party.logoPhoto);
            partyData.remove(party);
            dao.deleteParty(party);

            runOnUiThread {

                adapter.notifyDataSetChanged()
            }
        }.start()
    }
    private fun selectPhoto(){
        val intent = Intent(Intent.ACTION_PICK);
        intent.type = "image/*";
        selectPhotoLauncher.launch(intent);

    }
    private var selectPhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        if (result.resultCode == Activity.RESULT_OK) {
            imgUri = result.data?.data;
            imgToParty.setImageURI(imgUri);
        }
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
                val txtNumber = itemView.findViewById<TextView>(R.id.txt_party_number);

                val imgPhoto = itemView.findViewById<ImageView>(R.id.img_logo_party);

                val imgDelet = itemView.findViewById<ImageView>(R.id.img_icon_delet);

                if(item.logoPhoto != null){
                    imgPhoto.setImageURI(item.logoPhoto.toUri());
                }

                txtName.text = item.name;
                txtInitial.text = item.initials;
                txtNumber.text = item.number;

                imgDelet.setOnClickListener {
                    Log.i("teste","clickdelete")
                    deletParty(item);
                }

            }
        }

    }



}
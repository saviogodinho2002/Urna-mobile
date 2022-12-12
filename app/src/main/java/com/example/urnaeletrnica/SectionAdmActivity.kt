package com.example.urnaeletrnica;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView;
import com.example.urnaeletrnica.controllers.DataBankZoneController

class SectionAdmActivity : AppCompatActivity() {
    private lateinit var  dropZoneNumbers:AutoCompleteTextView;
    private lateinit var zoneController:DataBankZoneController
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_adm);

        val app = application as App

        val daoZone = app.db.ZoneDao()
        val daoSection = app.db.SectionDao()

         zoneController = DataBankZoneController(applicationContext,contentResolver,daoZone)

        dropZoneNumbers = findViewById(R.id.auto_section)

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
}
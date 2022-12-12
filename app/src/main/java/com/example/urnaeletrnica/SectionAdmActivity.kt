package com.example.urnaeletrnica;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView;
import com.example.urnaeletrnica.controllers.DataBankSectionController
import com.example.urnaeletrnica.controllers.DataBankZoneController

class SectionAdmActivity : AppCompatActivity() {
    private lateinit var dropZoneNumbers:AutoCompleteTextView;
    private lateinit var zoneController:DataBankZoneController
    private lateinit var sectionController:DataBankSectionController

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_adm);

        val app = application as App

        val daoZone = app.db.ZoneDao()
        val daoSection = app.db.SectionDao()

        zoneController = DataBankZoneController(applicationContext,contentResolver,daoZone)
        sectionController = DataBankSectionController(applicationContext,contentResolver,daoSection)

        dropZoneNumbers = findViewById(R.id.auto_section)

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
}
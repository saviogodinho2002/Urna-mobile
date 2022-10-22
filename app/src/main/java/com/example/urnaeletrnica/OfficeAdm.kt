package com.example.urnaeletrnica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.urnaeletrnica.model.entities.Candidate

class OfficeAdm : AppCompatActivity() {


    private lateinit var editOfficeName: EditText;
    private lateinit var editOfficeMaxNumber: EditText;

    private lateinit var btnSave: Button;
    private lateinit var btnUpdate: Button;
    private lateinit var recyclerView: RecyclerView;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_office_adm)

        editOfficeName = findViewById(R.id.edit_office_name);
        editOfficeMaxNumber = findViewById(R.id.edit_office_initials);
        btnSave = findViewById(R.id.btn_office_save);
        btnUpdate = findViewById(R.id.btn_office_update);

        recyclerView = findViewById(R.id.recycler_office);

        btnSave.setOnClickListener {

        }
        btnUpdate.setOnClickListener {

        }

    }




}
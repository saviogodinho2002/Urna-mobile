package com.example.urnaeletrnica

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var btnPartyActivity:Button;
    private lateinit var btnOfficeActivity: Button;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);

        //supportActionBar?.hide()

        btnPartyActivity = findViewById(R.id.btn_view_partys);
        btnOfficeActivity = findViewById(R.id.btn_office);

        btnOfficeActivity.setOnClickListener {
            val intent = Intent(this@MainActivity,OfficeAdm::class.java)
            startActivity(intent)
        }

        btnPartyActivity.setOnClickListener {
            val intent = Intent(this@MainActivity,PartysAdm::class.java);
            startActivity(intent);
        }

    }

}
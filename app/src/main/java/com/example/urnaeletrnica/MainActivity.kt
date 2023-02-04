package com.example.urnaeletrnica

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var btnPartyActivity:Button;
    private lateinit var btnOfficeActivity: Button;
    private lateinit var btnZoneAdmActivity: Button;
    private lateinit var btnSectionAdmActivity: Button;
    private lateinit var btnVoterAdmActivity: Button;
    private lateinit var btnCandidateAdmActivity: Button
    private lateinit var btnPlateAdmActivity: Button
    private lateinit var btnVote:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);


        if ( ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            }
        }
        if ( ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 2)
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 2)
            }
        }


        btnPartyActivity = findViewById(R.id.btn_view_partys);
        btnOfficeActivity = findViewById(R.id.btn_office);
        btnZoneAdmActivity = findViewById(R.id.btn_zone)
        btnSectionAdmActivity = findViewById(R.id.btn_section)
        btnVoterAdmActivity = findViewById(R.id.btn_voter_activity)
        btnCandidateAdmActivity = findViewById(R.id.btn_candidates)
        btnPlateAdmActivity = findViewById(R.id.btn_plate)
        btnVote = findViewById(R.id.btn_vote)


        btnOfficeActivity.setOnClickListener {
            val intent = Intent(this@MainActivity,OfficeAdmActivity::class.java)
            startActivity(intent)
        }

        btnPartyActivity.setOnClickListener {
            val intent = Intent(this@MainActivity,PartysAdmActivity::class.java);
            startActivity(intent);
        }
        btnZoneAdmActivity.setOnClickListener {
            val intent = Intent(this@MainActivity,ZoneAdmActivity::class.java);
            startActivity(intent)
        }
       btnSectionAdmActivity.setOnClickListener {
           val intent = Intent(this@MainActivity,SectionAdmActivity::class.java)
           startActivity(intent)
       }
        btnVoterAdmActivity.setOnClickListener {
            val intent = Intent(this@MainActivity, VoterAdmActivity::class.java)
            startActivity(intent)
        }
        btnCandidateAdmActivity.setOnClickListener {
            val intent = Intent(this@MainActivity,CandidateAdmActivity::class.java)
            startActivity(intent)
        }
        btnPlateAdmActivity.setOnClickListener {
            val intent = Intent(this@MainActivity,PlateAdmActivity::class.java)
            startActivity(intent)
        }
        btnVote.setOnClickListener {
            val intent = Intent(this@MainActivity,AutenticateVoter::class.java)
            startActivity(intent)
        }
    }

}
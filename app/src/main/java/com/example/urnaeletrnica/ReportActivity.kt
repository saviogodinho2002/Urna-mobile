package com.example.urnaeletrnica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.w3c.dom.Text

class ReportActivity : AppCompatActivity() {
    private lateinit var txtReport:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        txtReport = findViewById(R.id.report_txt)
        txtReport.text = intent.extras?.getString("report")!!

    }
}
package com.example.meditech.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.meditech.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        Log.d("MediTechDebug", "LoginActivity onCreate started")

        setContentView(R.layout.activity_login)
        Log.d("MediTechDebug", "LoginActivity setContentView finished")

        val loginBtn = findViewById<Button>(R.id.loginBtn)

        loginBtn.setOnClickListener {

            startActivity(
                Intent(this, DashboardActivity::class.java)
            )

        }
    }
}
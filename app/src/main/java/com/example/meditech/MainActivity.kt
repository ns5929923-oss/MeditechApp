package com.example.meditech

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.meditech.activities.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MediTechDebug", "MainActivity onCreate - Direct redirect to Login")
        
        // Direct redirect to Login to minimize main thread work
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
package com.example.josephsproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val trafficButton = findViewById<Button>(R.id.button7)
        trafficButton.setOnClickListener{
            Toast.makeText(this,"Traffic",Toast.LENGTH_SHORT).show()
        }

        val moviesButton = findViewById<Button>(R.id.button5)
        moviesButton.setOnClickListener {
            Toast.makeText(this,"Movies",Toast.LENGTH_SHORT).show()
        }

        val parksButton = findViewById<Button>(R.id.button8)
        parksButton.setOnClickListener {
            Toast.makeText(this,"Parks",Toast.LENGTH_SHORT).show()
        }

        val citiesButton = findViewById<Button>(R.id.button4)
        citiesButton.setOnClickListener {
            Toast.makeText(this,"Cities",Toast.LENGTH_SHORT).show()
        }

        val loginButton = findViewById<Button>(R.id.button)
        loginButton.setOnClickListener {
            Toast.makeText(this,"Login",Toast.LENGTH_SHORT).show()
        }

    }
}
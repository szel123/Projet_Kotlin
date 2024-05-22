package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class DonnesActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.donnees)

        val textDonnes : TextView = findViewById(R.id.textDonnees)
        textDonnes.setTextColor(ContextCompat.getColor(this, R.color.blueBar))



        val imageBluetooth : ImageView = findViewById(R.id.imageBluetooth)
        imageBluetooth.visibility = View.INVISIBLE
        val imageCalibrage: ImageView = findViewById(R.id.imageCalibrage)
        imageCalibrage.visibility = View.INVISIBLE
        val imageDonnees: ImageView = findViewById(R.id.imageDonnees)
        imageDonnees.visibility = View.VISIBLE
        val imageNotif: ImageView = findViewById(R.id.imageNotif)
        imageNotif.visibility = View.INVISIBLE
    }
}
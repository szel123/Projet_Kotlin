package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    var prothese_choisie : String = "None"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val imageBluetooth : ImageView = findViewById(R.id.imageBluetooth)
        imageBluetooth.visibility = View.VISIBLE
        val textBluetooth : TextView = findViewById(R.id.textBluetooth)
        textBluetooth.setTextColor(ContextCompat.getColor(this, R.color.blueBar))


        val imageCalibrage: ImageView = findViewById(R.id.imageCalibrage)
        imageCalibrage.visibility = View.INVISIBLE
        val imageDonnees: ImageView = findViewById(R.id.imageDonnees)
        imageDonnees.visibility = View.INVISIBLE
        val imageNotif: ImageView = findViewById(R.id.imageNotif)
        imageNotif.visibility = View.INVISIBLE


        val apparainage: LinearLayout = findViewById(R.id.apparainage)
        val calibrage: LinearLayout = findViewById(R.id.calibrage)
        val notifications: LinearLayout = findViewById(R.id.notifications)
        val donnees: LinearLayout = findViewById(R.id.donnees)

        calibrage.setOnClickListener {
            // Action to perform when the first LinearLayout is clicked
            //Toast.makeText(this, "calibrage", Toast.LENGTH_SHORT).show()
            if (prothese_choisie != "None"){

                try {
                    val intent = Intent(this@MainActivity, CalibrageActivity::class.java)
                    intent.putExtra("NomProthese", prothese_choisie)
                    startActivity(intent)
                } catch (e: Exception) {
                    println(" ${e.printStackTrace()}")
                    // Handle the exception or log the error message
                }
            }else{
                Toast.makeText(this@MainActivity,"choisir une prothese d'abord",Toast.LENGTH_SHORT).show()
            }
        }


        notifications.setOnClickListener {
            try {
                val intent = Intent(this@MainActivity, NotificationActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
            }

        }

        donnees.setOnClickListener {
            // Action to perform when the first LinearLayout is clicked
            if (prothese_choisie != "None"){

                try {
                    val intent = Intent(this@MainActivity, DonnesActivity::class.java)
                    intent.putExtra("NomProthese", prothese_choisie)
                    startActivity(intent)
                } catch (e: Exception) {
                    println("${e.printStackTrace()}")
                    // Handle the exception or log the error message
                }
            }else{
                Toast.makeText(this@MainActivity,"choisir une prothese d'abord",Toast.LENGTH_SHORT).show()
            }
        }

        val prothesesList = mutableListOf<Pair<String, Int>>()

        try {
            FirebaseApp.initializeApp(this)
            // Obtenir une instance de la base de données
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val myRef: DatabaseReference = database.getReference("protheses")

            myRef.get().addOnSuccessListener { dataSnapshot ->
                val prothesesList = mutableListOf<Pair<String, Int>>()

                for (protheseSnapshot in dataSnapshot.children) {
                    val protheseName = protheseSnapshot.key // Get the name of the prothesis (e.g., "prothese main", "prothese pied")
                    val signal = protheseSnapshot.child("signal").getValue(Int::class.java)
                    println("ilog1 ${protheseName} ${signal}")
                    prothesesList.add(Pair(protheseName!!, signal!!) as Pair<String, Int>)
                }

                // Now that we have the list, we can update the UI
                val parentLayout: LinearLayout = findViewById(R.id.parentLayout)

                for (prothese in prothesesList) {
                    val protheseLayout = LinearLayout(this).apply {
                        orientation = LinearLayout.HORIZONTAL
                        setBackgroundResource(R.drawable.layout_gray_dark)
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            150
                        ).apply {
                            setMargins(16, 16, 16, 16) // Add margins as needed
                        }
                        setOnClickListener {
                            Toast.makeText(this@MainActivity, "${prothese.first} choisie!", Toast.LENGTH_SHORT).show()
                            prothese_choisie = prothese.first
                        }
                    }

                    // Add TextView for the prothesis name
                    val textView = TextView(this).apply {
                        text = prothese.first
                        setTextColor(Color.BLACK)
                        textSize = 20f
                        layoutParams = LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1f
                        ).apply {
                            setMargins(50, 35, 16, 16) // Add margins as needed
                        }
                    }
                    protheseLayout.addView(textView)

                    // Add ImageView (you might want to set a proper image resource here)
                    val imageView = ImageView(this).apply {
                        if (prothese.second == 0)
                            setImageResource(R.drawable.wifi0)
                        else if (prothese.second == 1)
                            setImageResource(R.drawable.wifi1)
                        else if (prothese.second == 2)
                            setImageResource(R.drawable.wifi2)
                        else if (prothese.second == 3)
                            setImageResource(R.drawable.wifi3)
                        layoutParams = LinearLayout.LayoutParams(
                            100,
                            100
                        ).apply {
                            setMargins(16, 16, 45, 16)
                        }
                        }
                    protheseLayout.addView(imageView)

                    // Add the protheseLayout to the parent layout
                    parentLayout.addView(protheseLayout)
                }

            }.addOnFailureListener { error ->
                println("Failed to read value: ${error.message}")
            }
        } catch (e : Exception) {
            println(e)
        }
    }
}
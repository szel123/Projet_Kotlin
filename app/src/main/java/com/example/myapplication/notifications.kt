package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import androidx.compose.ui.unit.dp
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class NotificationActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notifications)

        val textCalibrage : TextView = findViewById(R.id.textNotification)
        textCalibrage.setTextColor(ContextCompat.getColor(this, R.color.blueBar))



        val imageBluetooth : ImageView = findViewById(R.id.imageBluetooth)
        imageBluetooth.visibility = View.INVISIBLE
        val imageCalibrage: ImageView = findViewById(R.id.imageCalibrage)
        imageCalibrage.visibility = View.INVISIBLE
        val imageDonnees: ImageView = findViewById(R.id.imageDonnees)
        imageDonnees.visibility = View.INVISIBLE
        val imageNotif: ImageView = findViewById(R.id.imageNotif)
        imageNotif.visibility = View.VISIBLE


        try {
            FirebaseApp.initializeApp(this)
            // Obtenir une instance de la base de données
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val myRef: DatabaseReference = database.getReference("notifUrg")
            val myRef2: DatabaseReference = database.getReference("notifNorm")


            myRef.get().addOnSuccessListener { Snapshot ->
                val NotifUrgList = mutableListOf<String>()

                for (dataSnapshot in Snapshot.children) {
                    val data = dataSnapshot.getValue(String::class.java)
                    if (data != null) {
                        NotifUrgList.add(data)
                    }
                }

                // Now that we have the list, we can update the UI
                val parentLayout: LinearLayout = findViewById(R.id.notifUrg)

                for (notif in NotifUrgList) {
                    val NotifLayout = LinearLayout(this).apply {
                        orientation = LinearLayout.HORIZONTAL
                        setBackgroundResource(R.drawable.layout_notif_urg)
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(16, 16, 16, 16) // Add margins as needed
                        }
                        setOnClickListener {
                            // action
                        }
                    }

                    // Add TextView for the Notif
                    val textView = TextView(this).apply {
                        text = notif
                        setTextColor(ContextCompat.getColor(this@NotificationActivity, R.color.redNotif))
                        textSize = 20f
                        layoutParams = LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1f
                        ).apply {
                            setMargins(50, 35, 16, 16) // Add margins as needed
                        }
                    }
                    NotifLayout.addView(textView)

                    // Add ImageView (Later)


                    // Add the protheseLayout to the parent layout
                    parentLayout.addView(NotifLayout)
                }

            }.addOnFailureListener { error ->
                println("Failed to read value: ${error.message}")
            }



            myRef2.get().addOnSuccessListener { Snapshot ->
                val NotifNormList = mutableListOf<String>()

                for (dataSnapshot in Snapshot.children) {
                    val data = dataSnapshot.getValue(String::class.java)
                    if (data != null) {
                        NotifNormList.add(data)
                    }
                }

                // Now that we have the list, we can update the UI
                val parentLayout: LinearLayout = findViewById(R.id.notifNorm)

                for (notif in NotifNormList) {
                    val NotifLayout = LinearLayout(this).apply {
                        orientation = LinearLayout.HORIZONTAL
                        setBackgroundResource(R.drawable.layout_notif_norm)
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(16, 16, 16, 16) // Add margins as needed
                        }
                        setOnClickListener {
                            // action
                        }
                    }

                    // Add TextView for the Notif
                    val textView = TextView(this).apply {
                        text = notif
                        setTextColor(ContextCompat.getColor(this@NotificationActivity, R.color.greenNotif))
                        textSize = 20f
                        layoutParams = LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1f
                        ).apply {
                            setMargins(50, 35, 16, 16) // Add margins as needed
                        }
                    }
                    NotifLayout.addView(textView)

                    // Add ImageView (Later)


                    // Add the protheseLayout to the parent layout
                    parentLayout.addView(NotifLayout)
                }

            }.addOnFailureListener { error ->
                println("Failed to read value: ${error.message}")
            }
        } catch (e : Exception) {
            println(e)
        }

    }
}
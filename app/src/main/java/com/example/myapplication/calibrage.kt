package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CalibrageActivity : AppCompatActivity() {

    lateinit var textSensibilite : TextView
    lateinit var textangle : TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calibrage)

        val textCalibrage : TextView = findViewById(R.id.textCalibrage)
        textCalibrage.setTextColor(ContextCompat.getColor(this, R.color.blueBar))



        val imageBluetooth : ImageView = findViewById(R.id.imageBluetooth)
        imageBluetooth.visibility = View.INVISIBLE
        val imageCalibrage: ImageView = findViewById(R.id.imageCalibrage)
        imageCalibrage.visibility = View.VISIBLE
        val imageDonnees: ImageView = findViewById(R.id.imageDonnees)
        imageDonnees.visibility = View.INVISIBLE
        val imageNotif: ImageView = findViewById(R.id.imageNotif)
        imageNotif.visibility = View.INVISIBLE

        val intent = intent
        val nomProthese = intent.getStringExtra("NomProthese")


        textSensibilite = findViewById(R.id.sensibilite)
        textSensibilite.text = "sensibilite"

        textangle = findViewById(R.id.angle)
        textangle.text = "angle"

        val plus: ImageView = findViewById(R.id.plus)
        val minus: ImageView = findViewById(R.id.minus)
        val plus2: ImageView = findViewById(R.id.plus2)
        val minus2: ImageView = findViewById(R.id.minus2)
        val indic: View = findViewById(R.id.indicatorView)
        val indic2: View = findViewById(R.id.indicatorView2)

        fun dpToPx(dp: Int): Int {
            return (dp * Resources.getSystem().displayMetrics.density).toInt()
        }


        try {
            FirebaseApp.initializeApp(this)
            // Obtenir une instance de la base de données
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val myRef: DatabaseReference = database.getReference("protheses")
            myRef.get().addOnSuccessListener { dataSnapshot ->
                for (protheseSnapshot in dataSnapshot.children) {
                    if(protheseSnapshot.key == nomProthese){
                        var sens = protheseSnapshot.child("sensibilite").getValue(Int::class.java)
                        textSensibilite.text = "sensibilite : $sens %"
                        var wid : Int = sens as Int * 200 /100
                        indic.layoutParams.width  = dpToPx(wid)
                        indic.requestLayout()
                        println("asigo $sens $wid")
                        var ang = protheseSnapshot.child("angle").getValue(Int::class.java)
                        textangle.text = "angle : $ang degres"
                        wid = dpToPx(ang as Int * 200/180)
                        indic2.layoutParams.width  = wid


                        plus.setOnClickListener {
                            for (protheseSnapshot in dataSnapshot.children) {
                                if (protheseSnapshot.key == nomProthese) {
                                    val protheseRef = protheseSnapshot.child("sensibilite").ref
                                    // Add a listener to fetch the latest value from the database
                                    protheseRef.addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            val currentValue: Int? = dataSnapshot.getValue(Int::class.java)
                                            val newValue: Int? = currentValue?.plus(5)
                                            newValue?.let {
                                                if (it <= 100) {
                                                    protheseRef.setValue(it)
                                                        .addOnSuccessListener {
                                                            // Update UI after successfully updating the value in the database
                                                            textSensibilite.text = "sensibilite : $newValue %"
                                                            var wid: Int = newValue * 200 / 100
                                                            indic.layoutParams.width = dpToPx(wid)
                                                            indic.requestLayout()
                                                        }
                                                        .addOnFailureListener { e ->
                                                            // Handle failure
                                                        }
                                                } else {
                                                    // Handle case where newValue exceeds maximum threshold
                                                }
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }


                                    })
                                }
                            }
                        }


                        minus.setOnClickListener {
                            for (protheseSnapshot in dataSnapshot.children) {
                                if (protheseSnapshot.key == nomProthese) {
                                    val protheseRef = protheseSnapshot.child("sensibilite").ref
                                    // Add a listener to fetch the latest value from the database
                                    protheseRef.addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            val currentValue: Int? = dataSnapshot.getValue(Int::class.java)
                                            val newValue: Int? = currentValue?.minus(5)
                                            newValue?.let {
                                                if (it >= 0) {
                                                    protheseRef.setValue(it)
                                                        .addOnSuccessListener {
                                                            // Update UI after successfully updating the value in the database
                                                            textSensibilite.text = "sensibilite : $newValue %"
                                                            var wid: Int = newValue * 200 / 100
                                                            indic.layoutParams.width = dpToPx(wid)
                                                            indic.requestLayout()
                                                        }
                                                        .addOnFailureListener { e ->
                                                            // Handle failure
                                                        }
                                                } else {
                                                    // Handle case where newValue exceeds maximum threshold
                                                }
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }


                                    })
                                }
                            }
                        }


                        plus2.setOnClickListener {
                            for (protheseSnapshot in dataSnapshot.children) {
                                if (protheseSnapshot.key == nomProthese) {
                                    val protheseRef = protheseSnapshot.child("angle").ref
                                    // Add a listener to fetch the latest value from the database
                                    protheseRef.addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            val currentValue: Int? = dataSnapshot.getValue(Int::class.java)
                                            val newValue: Int? = currentValue?.plus(5)
                                            newValue?.let {
                                                if (it <= 180) {
                                                    protheseRef.setValue(it)
                                                        .addOnSuccessListener {
                                                            // Update UI after successfully updating the value in the database
                                                            textangle.text = "angle : $newValue degre"
                                                            var wid: Int = newValue * 200 / 180
                                                            indic2.layoutParams.width = dpToPx(wid)
                                                            indic2.requestLayout()
                                                        }
                                                        .addOnFailureListener { e ->
                                                            // Handle failure
                                                        }
                                                } else {
                                                    // Handle case where newValue exceeds maximum threshold
                                                }
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }


                                    })
                                }
                            }
                        }


                        minus2.setOnClickListener {
                            for (protheseSnapshot in dataSnapshot.children) {
                                if (protheseSnapshot.key == nomProthese) {
                                    val protheseRef = protheseSnapshot.child("angle").ref
                                    // Add a listener to fetch the latest value from the database
                                    protheseRef.addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            val currentValue: Int? = dataSnapshot.getValue(Int::class.java)
                                            val newValue: Int? = currentValue?.minus(5)
                                            newValue?.let {
                                                if (it >= 0) {
                                                    protheseRef.setValue(it)
                                                        .addOnSuccessListener {
                                                            // Update UI after successfully updating the value in the database
                                                            textangle.text = "angle : $newValue degre"
                                                            var wid: Int = newValue * 200 / 180
                                                            indic2.layoutParams.width = dpToPx(wid)
                                                            indic2.requestLayout()
                                                        }
                                                        .addOnFailureListener { e ->
                                                            // Handle failure
                                                        }
                                                } else {
                                                    // Handle case where newValue exceeds maximum threshold
                                                }
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }


                                    })
                                }
                            }
                        }













                    }
                }
            }





        }catch (e :Exception){

        }





        val apparainage: LinearLayout = findViewById(R.id.apparainage)
        val calibrage: LinearLayout = findViewById(R.id.calibrage)
        val notifications: LinearLayout = findViewById(R.id.notifications)
        val donnees: LinearLayout = findViewById(R.id.donnees)

        notifications.setOnClickListener {
            try {
                val intent = Intent(this@CalibrageActivity, NotificationActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
            }

        }

        apparainage.setOnClickListener {
                try {
                    val intent = Intent(this@CalibrageActivity, MainActivity::class.java)
                    startActivity(intent)
                } catch (e: Exception) {
                }

        }

        donnees.setOnClickListener {


                try {
                    val intent = Intent(this@CalibrageActivity, DonnesActivity::class.java)
                    intent.putExtra("NomProthese", nomProthese)
                    startActivity(intent)
                } catch (e: Exception) {
                    println("${e.printStackTrace()}")
                }

        }




    }
}
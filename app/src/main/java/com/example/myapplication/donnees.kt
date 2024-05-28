package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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


class DonnesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.donnees)

        val intent = intent
        val nomProthese = intent.getStringExtra("NomProthese")


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



        val pointsData: List<Point> = listOf()
        val MutpointsData : MutableList<Point> = pointsData.toMutableList()

        val pointsData2: List<Point> = listOf()
        val MutpointsData2 : MutableList<Point> = pointsData.toMutableList()



        FirebaseApp.initializeApp(this)
        // Obtenir une instance de la base de données
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("protheses")
        myRef.get().addOnSuccessListener { dataSnapshot ->
            for (protheseSnapshot in dataSnapshot.children) {


                if (protheseSnapshot.key == nomProthese) {
                    val tempSnapshot = protheseSnapshot.child("temp")

                    // Parcourir les enfants du snapshot "temp"
                    for (child in tempSnapshot.children) {
                        val key = child.key?.toIntOrNull()
                        val value = child.getValue(Float::class.java)
                        if (key != null && value != null) {
                            MutpointsData.add(Point(key.toFloat(),value))
                        }
                    }
                }
            }

            val composeView = findViewById<ComposeView>(R.id.composeView)
            composeView.setContent {
                LineChartContent(MutpointsData)
            }
        }



        myRef.get().addOnSuccessListener { dataSnapshot ->
            for (protheseSnapshot in dataSnapshot.children) {


                if (protheseSnapshot.key == nomProthese) {
                    val tempSnapshot = protheseSnapshot.child("card")

                    // Parcourir les enfants du snapshot "temp"
                    for (child in tempSnapshot.children) {
                        val key = child.key?.toIntOrNull()
                        val value = child.getValue(Float::class.java)
                        if (key != null && value != null) {
                            MutpointsData2.add(Point(key.toFloat(),value))
                        }
                    }
                }
            }

            val composeView2 = findViewById<ComposeView>(R.id.composeView2)
            composeView2.setContent {
                LineChartContent(MutpointsData2)
            }
        }

        val apparainage: LinearLayout = findViewById(R.id.apparainage)
        val calibrage: LinearLayout = findViewById(R.id.calibrage)
        val notifications: LinearLayout = findViewById(R.id.notifications)
        val donnees: LinearLayout = findViewById(R.id.donnees)



        apparainage.setOnClickListener {
            try {
                val intent = Intent(this@DonnesActivity, MainActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
            }

        }

        calibrage.setOnClickListener {


            try {
                val intent = Intent(this@DonnesActivity, CalibrageActivity::class.java)
                intent.putExtra("NomProthese", nomProthese)
                startActivity(intent)
            } catch (e: Exception) {
                println("${e.printStackTrace()}")
            }

        }


    }
}

@Composable
fun LineChartContent(pointsData: List<Point>) {
    // Find the minimum and maximum y values in pointsData
    val minY = pointsData.minOfOrNull { it.y } ?: 0
    val maxY = pointsData.maxOfOrNull { it.y } ?: 0
    val yRange = maxY.toFloat() - minY.toFloat()

    // Define the number of steps for the y-axis
    val steps: Int = 5
    val yScale = if (yRange != 0f) yRange / steps else 1

    // Create the x-axis data
    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .backgroundColor(androidx.compose.ui.graphics.Color.White)
        .steps(pointsData.size - 1)
        .labelData { i -> i.toString() }
        .labelAndAxisLinePadding(15.dp)
        .build()

    // Create the y-axis data
    val yAxisData = AxisData.Builder()
        .steps(steps)
        .backgroundColor(androidx.compose.ui.graphics.Color.White)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i -> (minY.toFloat() + i * yScale.toFloat()).toString() }
        .build()

    // Scale the pointsData to fit the y-axis
    val scaledPointsData = pointsData.map { point ->
        Point(point.x, ((point.y - minY.toFloat()) * steps / yRange))
    }

    // Create the line chart data
    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = scaledPointsData,
                    LineStyle(),
                    IntersectionPoint(),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp()
                )
            ),
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(),
        backgroundColor = androidx.compose.ui.graphics.Color.White
    )

    // Display the line chart
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        lineChartData = lineChartData
    )
}
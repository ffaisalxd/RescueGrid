package com.example.rescuegrid

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HistoryActivity : AppCompatActivity() {

    private lateinit var historyContainer: LinearLayout
    private lateinit var emptyText: TextView
    private lateinit var historyCountText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_history)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.historyMain)
        ) { view, insets ->

            val bars =
                insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                )

            view.setPadding(
                bars.left,
                bars.top,
                bars.right,
                bars.bottom
            )

            insets
        }

        findViewById<LinearLayout>(
            R.id.backContainer
        ).setOnClickListener {
            finish()
        }

        historyContainer =
            findViewById(
                R.id.historyContainer
            )

        emptyText =
            findViewById(
                R.id.emptyHistoryText
            )

        historyCountText =
            findViewById(
                R.id.historyCountText
            )

        displayHistory()
    }

    override fun onResume() {
        super.onResume()
        displayHistory()
    }

    private fun displayHistory() {

        historyContainer.removeAllViews()

        val historyItems =
            RescueData.reservationList.filter {
                it.status == "Collected" ||
                        it.status == "Cancelled"
            }

        historyCountText.text =
            "${historyItems.size} records"

        if (historyItems.isEmpty()) {

            emptyText.visibility =
                View.VISIBLE

            return
        }

        emptyText.visibility =
            View.GONE

        for (reservation in historyItems.reversed()) {

            val itemView =
                LayoutInflater.from(this)
                    .inflate(
                        R.layout.item_history,
                        historyContainer,
                        false
                    )

            val image =
                itemView.findViewById<ImageView>(
                    R.id.historyFoodImage
                )

            val foodTitle =
                itemView.findViewById<TextView>(
                    R.id.historyFoodTitle
                )

            val pickup =
                itemView.findViewById<TextView>(
                    R.id.historyPickupText
                )

            val status =
                itemView.findViewById<TextView>(
                    R.id.historyStatusText
                )

            val area =
                itemView.findViewById<TextView>(
                    R.id.historyAreaText
                )

            val food =
                RescueData.findFoodById(
                    reservation.foodId
                )

            foodTitle.text =
                reservation.foodTitle

            pickup.text =
                reservation.pickupDateTime

            status.text =
                reservation.status

            area.text =
                food?.area ?: "Collection area"

            FoodImageLoader.load(
                image,
                food?.imageUri ?: ""
            )

            historyContainer.addView(
                itemView
            )
        }
    }
}
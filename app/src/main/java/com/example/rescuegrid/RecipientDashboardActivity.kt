package com.example.rescuegrid

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView

class RecipientDashboardActivity : AppCompatActivity() {

    private lateinit var availableFoodText: TextView
    private lateinit var reservationCountText: TextView
    private lateinit var collectedCountText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recipient_dashboard)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.recipientDashboardMain)
        ) { view, insets ->

            val systemBars =
                insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                )

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        availableFoodText =
            findViewById(R.id.availableFoodText)

        reservationCountText =
            findViewById(R.id.reservationCountText)

        collectedCountText =
            findViewById(R.id.recipientCollectedText)

        val discoverCard =
            findViewById<MaterialCardView>(
                R.id.discoverCard
            )

        val reservationsCard =
            findViewById<MaterialCardView>(
                R.id.reservationsCard
            )

        val historyCard =
            findViewById<MaterialCardView>(
                R.id.recipientHistoryCard
            )

        val profileCard =
            findViewById<MaterialCardView>(
                R.id.recipientProfileCard
            )

        discoverCard.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    DiscoverFoodActivity::class.java
                )
            )
        }

        reservationsCard.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    MyReservationsActivity::class.java
                )
            )
        }

        historyCard.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    HistoryActivity::class.java
                )
            )
        }

        profileCard.setOnClickListener {

            val intent =
                Intent(
                    this,
                    ProfileActivity::class.java
                )

            intent.putExtra(
                "userRole",
                "Recipient"
            )

            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        updateDashboard()
    }

    private fun updateDashboard() {

        availableFoodText.text =
            RescueData.foodList.count {
                it.status == "Available"
            }.toString()

        reservationCountText.text =
            RescueData.reservationList.count {
                it.status != "Cancelled" &&
                        it.status != "Collected"
            }.toString()

        collectedCountText.text =
            RescueData.reservationList.count {
                it.status == "Collected"
            }.toString()
    }
}
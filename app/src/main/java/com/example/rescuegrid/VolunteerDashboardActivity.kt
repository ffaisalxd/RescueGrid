package com.example.rescuegrid

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView

class VolunteerDashboardActivity : AppCompatActivity() {

    private lateinit var availableTasksText: TextView
    private lateinit var activeTasksText: TextView
    private lateinit var completedTasksText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_volunteer_dashboard)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.volunteerDashboardMain)
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

        availableTasksText =
            findViewById(
                R.id.availableTasksText
            )

        activeTasksText =
            findViewById(
                R.id.activeTasksText
            )

        completedTasksText =
            findViewById(
                R.id.completedTasksText
            )

        val tasksCard =
            findViewById<MaterialCardView>(
                R.id.collectionTasksCard
            )

        val activeCard =
            findViewById<MaterialCardView>(
                R.id.activeCollectionsCard
            )

        val historyCard =
            findViewById<MaterialCardView>(
                R.id.volunteerHistoryCard
            )

        val profileCard =
            findViewById<MaterialCardView>(
                R.id.volunteerProfileCard
            )

        tasksCard.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    CollectionTasksActivity::class.java
                )
            )
        }

        activeCard.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ActiveCollectionsActivity::class.java
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
                "Volunteer Collector"
            )

            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        updateDashboard()
    }

    private fun updateDashboard() {

        availableTasksText.text =
            RescueData.reservationList.count {
                it.status == "Scheduled"
            }.toString()

        activeTasksText.text =
            RescueData.reservationList.count {
                it.status ==
                        "Collector Assigned" ||
                        it.status ==
                        "On The Way"
            }.toString()

        completedTasksText.text =
            RescueData.reservationList.count {
                it.status ==
                        "Collected"
            }.toString()
    }
}
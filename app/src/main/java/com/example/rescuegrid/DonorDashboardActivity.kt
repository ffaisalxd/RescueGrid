package com.example.rescuegrid

import android.content.Intent
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
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class DonorDashboardActivity : AppCompatActivity() {

    private lateinit var impactText: TextView
    private lateinit var activeListingsText: TextView
    private lateinit var completedText: TextView
    private lateinit var recentListingsContainer: LinearLayout
    private lateinit var noRecentListingsText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(
            R.layout.activity_donor_dashboard
        )

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.donorDashboardMain)
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

        impactText =
            findViewById(R.id.impactText)

        activeListingsText =
            findViewById(R.id.activeListingsText)

        completedText =
            findViewById(R.id.completedText)

        recentListingsContainer =
            findViewById(
                R.id.recentListingsContainer
            )

        noRecentListingsText =
            findViewById(
                R.id.noRecentListingsText
            )

        val addFoodButton =
            findViewById<MaterialButton>(
                R.id.addFoodButton
            )

        val activeDonationCard =
            findViewById<MaterialCardView>(
                R.id.activeDonationCard
            )

        val historyCard =
            findViewById<MaterialCardView>(
                R.id.historyCard
            )

        val profileCard =
            findViewById<MaterialCardView>(
                R.id.profileCard
            )

        addFoodButton.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    AddFoodActivity::class.java
                )
            )
        }

        activeDonationCard.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ActiveDonationsActivity::class.java
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
                "Donor"
            )

            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        updateDashboard()
    }

    private fun updateDashboard() {

        impactText.text =
            RescueData.getImpactCount()
                .toString()

        activeListingsText.text =
            RescueData
                .getActiveListingsCount()
                .toString()

        completedText.text =
            RescueData
                .getCompletedListingsCount()
                .toString()

        showRecentListings()
    }

    private fun showRecentListings() {

        recentListingsContainer.removeAllViews()

        val recentFood =
            RescueData.foodList
                .takeLast(3)
                .reversed()

        if (recentFood.isEmpty()) {

            noRecentListingsText.visibility =
                View.VISIBLE

            return
        }

        noRecentListingsText.visibility =
            View.GONE

        for (food in recentFood) {

            val view =
                LayoutInflater.from(this)
                    .inflate(
                        R.layout.item_food,
                        recentListingsContainer,
                        false
                    )

            val image =
                view.findViewById<ImageView>(
                    R.id.foodImage
                )

            val title =
                view.findViewById<TextView>(
                    R.id.foodTitleText
                )

            val category =
                view.findViewById<TextView>(
                    R.id.foodCategoryText
                )

            val quantity =
                view.findViewById<TextView>(
                    R.id.foodQuantityText
                )

            val area =
                view.findViewById<TextView>(
                    R.id.foodAreaText
                )

            val bestBefore =
                view.findViewById<TextView>(
                    R.id.foodBestBeforeText
                )

            val status =
                view.findViewById<TextView>(
                    R.id.foodStatusText
                )

            title.text =
                food.title

            category.text =
                food.category

            quantity.text =
                "${food.quantity} portions"

            area.text =
                food.area

            bestBefore.text =
                "Best before ${food.bestBefore}"

            status.text =
                food.status

            FoodImageLoader.load(
                image,
                food.imageUri
            )

            recentListingsContainer.addView(
                view
            )
        }
    }
}
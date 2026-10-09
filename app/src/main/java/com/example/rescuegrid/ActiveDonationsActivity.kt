package com.example.rescuegrid

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView

class ActiveDonationsActivity : AppCompatActivity() {

    private lateinit var foodContainer:
            LinearLayout

    private lateinit var emptyText:
            TextView

    private lateinit var listingCountText:
            TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(
            R.layout.activity_active_donations
        )

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.activeDonationsMain)
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

        val backContainer =
            findViewById<LinearLayout>(
                R.id.backContainer
            )

        foodContainer =
            findViewById(
                R.id.foodContainer
            )

        emptyText =
            findViewById(
                R.id.emptyText
            )

        listingCountText =
            findViewById(
                R.id.listingCountText
            )

        backContainer.setOnClickListener {
            finish()
        }

        displayFoodListings()
    }

    override fun onResume() {
        super.onResume()

        displayFoodListings()
    }

    private fun displayFoodListings() {

        foodContainer.removeAllViews()

        val activeFood =
            RescueData.foodList.filter {
                it.status != "Collected"
            }

        listingCountText.text =
            "${activeFood.size} active listings"

        if (activeFood.isEmpty()) {

            emptyText.visibility =
                View.VISIBLE

            return
        }

        emptyText.visibility =
            View.GONE

        for (
        food in
        activeFood.reversed()
        ) {

            val itemView =
                LayoutInflater.from(this)
                    .inflate(
                        R.layout.item_food,
                        foodContainer,
                        false
                    )

            val image =
                itemView.findViewById<ImageView>(
                    R.id.foodImage
                )

            val title =
                itemView.findViewById<TextView>(
                    R.id.foodTitleText
                )

            val category =
                itemView.findViewById<TextView>(
                    R.id.foodCategoryText
                )

            val quantity =
                itemView.findViewById<TextView>(
                    R.id.foodQuantityText
                )

            val area =
                itemView.findViewById<TextView>(
                    R.id.foodAreaText
                )

            val bestBefore =
                itemView.findViewById<TextView>(
                    R.id.foodBestBeforeText
                )

            val status =
                itemView.findViewById<TextView>(
                    R.id.foodStatusText
                )

            val card =
                itemView.findViewById<MaterialCardView>(
                    R.id.foodCard
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

            card.setOnClickListener {

                if (
                    food.status !=
                    "Available"
                ) {

                    Toast.makeText(
                        this,
                        "Reserved listings cannot be edited",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@setOnClickListener
                }

                val intent =
                    Intent(
                        this,
                        AddFoodActivity::class.java
                    )

                intent.putExtra(
                    "editFoodId",
                    food.id
                )

                startActivity(intent)
            }

            foodContainer.addView(
                itemView
            )
        }
    }
}
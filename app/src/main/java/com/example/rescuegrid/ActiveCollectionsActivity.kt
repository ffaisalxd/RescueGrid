package com.example.rescuegrid

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class ActiveCollectionsActivity : AppCompatActivity() {

    private lateinit var taskContainer: LinearLayout
    private lateinit var emptyText: TextView
    private lateinit var taskCountText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_active_collections)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.activeCollectionsMain)
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

        taskContainer =
            findViewById(
                R.id.activeCollectionContainer
            )

        emptyText =
            findViewById(
                R.id.emptyActiveCollectionText
            )

        taskCountText =
            findViewById(
                R.id.activeCollectionCountText
            )

        displayActiveCollections()
    }

    override fun onResume() {
        super.onResume()
        displayActiveCollections()
    }

    private fun displayActiveCollections() {

        taskContainer.removeAllViews()

        val tasks =
            RescueData.reservationList.filter {
                it.status == "Collector Assigned" ||
                        it.status == "On The Way"
            }

        taskCountText.text =
            "${tasks.size} active collections"

        if (tasks.isEmpty()) {

            emptyText.visibility =
                View.VISIBLE

            return
        }

        emptyText.visibility =
            View.GONE

        for (reservation in tasks.reversed()) {

            val row =
                LayoutInflater.from(this)
                    .inflate(
                        R.layout.item_collection_task,
                        taskContainer,
                        false
                    )

            val image =
                row.findViewById<ImageView>(
                    R.id.collectionFoodImage
                )

            val title =
                row.findViewById<TextView>(
                    R.id.collectionFoodTitle
                )

            val pickup =
                row.findViewById<TextView>(
                    R.id.collectionPickupText
                )

            val area =
                row.findViewById<TextView>(
                    R.id.collectionAreaText
                )

            val status =
                row.findViewById<TextView>(
                    R.id.collectionStatusText
                )

            val actionButton =
                row.findViewById<MaterialButton>(
                    R.id.collectionActionButton
                )

            val food =
                RescueData.findFoodById(
                    reservation.foodId
                )

            title.text =
                reservation.foodTitle

            pickup.text =
                reservation.pickupDateTime

            area.text =
                food?.area ?: "Collection area"

            status.text =
                reservation.status

            FoodImageLoader.load(
                image,
                food?.imageUri ?: ""
            )

            when (reservation.status) {

                "Collector Assigned" -> {

                    actionButton.text =
                        "Mark On The Way"

                    actionButton.setOnClickListener {

                        reservation.status =
                            "On The Way"

                        RescueData.saveData(
                            this
                        )

                        Toast.makeText(
                            this,
                            "Collection is now on the way",
                            Toast.LENGTH_SHORT
                        ).show()

                        displayActiveCollections()
                    }
                }

                "On The Way" -> {

                    actionButton.text =
                        "Mark Collected"

                    actionButton.setOnClickListener {

                        AlertDialog.Builder(this)
                            .setTitle(
                                "Complete collection"
                            )
                            .setMessage(
                                "Confirm that the food has been collected?"
                            )
                            .setNegativeButton(
                                "Cancel",
                                null
                            )
                            .setPositiveButton(
                                "Confirm"
                            ) { _, _ ->

                                reservation.status =
                                    "Collected"

                                if (food != null) {
                                    food.status =
                                        "Collected"
                                }

                                RescueData.saveData(
                                    this
                                )

                                Toast.makeText(
                                    this,
                                    "Collection completed",
                                    Toast.LENGTH_SHORT
                                ).show()

                                displayActiveCollections()
                            }
                            .show()
                    }
                }
            }

            taskContainer.addView(
                row
            )
        }
    }
}
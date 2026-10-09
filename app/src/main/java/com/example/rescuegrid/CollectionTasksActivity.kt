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

class CollectionTasksActivity : AppCompatActivity() {

    private lateinit var taskContainer: LinearLayout
    private lateinit var emptyText: TextView
    private lateinit var taskCountText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_collection_tasks)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.collectionTasksMain)
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
                R.id.collectionTaskContainer
            )

        emptyText =
            findViewById(
                R.id.emptyCollectionText
            )

        taskCountText =
            findViewById(
                R.id.taskCountText
            )

        displayTasks()
    }

    override fun onResume() {
        super.onResume()
        displayTasks()
    }

    private fun displayTasks() {

        taskContainer.removeAllViews()

        val tasks =
        RescueData.reservationList.filter {
            it.status == "Scheduled"
        }

        taskCountText.text =
            "${tasks.size} available tasks"

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

            val action =
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

            loadImage(
                image,
                food
            )

            when (reservation.status) {

                "Scheduled" -> {

                    action.text =
                        "Claim Collection"

                    action.setOnClickListener {

                        confirmAction(
                            "Claim collection",
                            "Accept this collection task?"
                        ) {

                            reservation.status =
                                "Collector Assigned"

                            reservation.volunteerName =
                                "Volunteer Collector"

                            RescueData.saveData(
                                this
                            )

                            displayTasks()
                        }
                    }
                }

                "Collector Assigned" -> {

                    action.text =
                        "Mark On The Way"

                    action.setOnClickListener {

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

                        displayTasks()
                    }
                }

                "On The Way" -> {

                    action.text =
                        "Mark Collected"

                    action.setOnClickListener {

                        confirmAction(
                            "Complete collection",
                            "Confirm that the food has been collected?"
                        ) {

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

                            displayTasks()
                        }
                    }
                }
            }

            taskContainer.addView(row)
        }
    }

    private fun confirmAction(
        title: String,
        message: String,
        action: () -> Unit
    ) {

        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(
                "Cancel",
                null
            )
            .setPositiveButton(
                "Confirm"
            ) { _, _ ->

                action()
            }
            .show()
    }

    private fun loadImage(
        imageView: ImageView,
        food: FoodItem?
    ) {

        imageView.imageTintList = null
        imageView.clearColorFilter()
        imageView.setPadding(
            0,
            0,
            0,
            0
        )

        imageView.scaleType =
            ImageView.ScaleType.CENTER_CROP

        if (food == null) {
            showPlaceholder(imageView)
            return
        }

        when (food.id) {

            1 -> {
                imageView.setImageResource(
                    R.drawable.bakery
                )
            }

            2 -> {
                imageView.setImageResource(
                    R.drawable.produce
                )
            }

            3 -> {
                imageView.setImageResource(
                    R.drawable.meals
                )
            }

            else -> {

                if (food.imageUri.isNotBlank()) {

                    try {

                        imageView.setImageURI(
                            Uri.parse(
                                food.imageUri
                            )
                        )

                    } catch (_: Exception) {

                        showPlaceholder(
                            imageView
                        )
                    }

                } else {

                    showPlaceholder(
                        imageView
                    )
                }
            }
        }
    }

    private fun showPlaceholder(
        imageView: ImageView
    ) {

        imageView.imageTintList = null
        imageView.clearColorFilter()

        imageView.setImageResource(
            android.R.drawable.ic_menu_gallery
        )

        imageView.scaleType =
            ImageView.ScaleType.CENTER_INSIDE
    }
}
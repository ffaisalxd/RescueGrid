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

class MyReservationsActivity : AppCompatActivity() {

    private lateinit var reservationContainer: LinearLayout
    private lateinit var emptyText: TextView
    private lateinit var reservationCountText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_my_reservations)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.myReservationsMain)
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

        reservationContainer =
            findViewById(
                R.id.reservationContainer
            )

        emptyText =
            findViewById(
                R.id.emptyReservationText
            )

        reservationCountText =
            findViewById(
                R.id.reservationCountText
            )

        backContainer.setOnClickListener {
            finish()
        }

        displayReservations()
    }

    override fun onResume() {
        super.onResume()
        displayReservations()
    }

    private fun displayReservations() {

        reservationContainer.removeAllViews()

        val activeReservations =
            RescueData.reservationList.filter {
                it.status != "Cancelled" &&
                        it.status != "Collected"
            }

        reservationCountText.text =
            "${activeReservations.size} active"

        if (activeReservations.isEmpty()) {

            emptyText.visibility =
                View.VISIBLE

            return
        }

        emptyText.visibility =
            View.GONE

        for (
        reservation in
        activeReservations.reversed()
        ) {

            val itemView =
                LayoutInflater.from(this)
                    .inflate(
                        R.layout.item_reservation,
                        reservationContainer,
                        false
                    )

            val image =
                itemView.findViewById<ImageView>(
                    R.id.reservationItemImage
                )

            val title =
                itemView.findViewById<TextView>(
                    R.id.reservationItemFoodTitle
                )

            val pickup =
                itemView.findViewById<TextView>(
                    R.id.reservationItemPickup
                )

            val status =
                itemView.findViewById<TextView>(
                    R.id.reservationItemStatus
                )

            val area =
                itemView.findViewById<TextView>(
                    R.id.reservationItemArea
                )

            val cancelButton =
                itemView.findViewById<MaterialButton>(
                    R.id.cancelReservationItemButton
                )

            val food =
                RescueData.findFoodById(
                    reservation.foodId
                )

            title.text =
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

            cancelButton.isEnabled =
                reservation.status == "Scheduled"

            cancelButton.alpha =
                if (
                    reservation.status ==
                    "Scheduled"
                ) {
                    1f
                } else {
                    0.45f
                }

            cancelButton.setOnClickListener {

                if (
                    reservation.status !=
                    "Scheduled"
                ) {

                    Toast.makeText(
                        this,
                        "This collection is already being handled",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@setOnClickListener
                }

                AlertDialog.Builder(this)
                    .setTitle(
                        "Cancel reservation"
                    )
                    .setMessage(
                        "Make ${reservation.foodTitle} available again?"
                    )
                    .setNegativeButton(
                        "Keep Reservation",
                        null
                    )
                    .setPositiveButton(
                        "Cancel Reservation"
                    ) { _, _ ->

                        reservation.status =
                            "Cancelled"

                        if (food != null) {
                            food.status =
                                "Available"
                        }

                        RescueData.saveData(
                            this
                        )

                        Toast.makeText(
                            this,
                            "Reservation cancelled",
                            Toast.LENGTH_SHORT
                        ).show()

                        displayReservations()
                    }
                    .show()
            }

            reservationContainer.addView(
                itemView
            )
        }
    }
}
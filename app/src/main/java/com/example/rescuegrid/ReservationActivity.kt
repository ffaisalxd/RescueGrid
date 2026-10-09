package com.example.rescuegrid

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
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
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReservationActivity : AppCompatActivity() {

    private lateinit var pickupDateTimeEditText: TextInputEditText
    private val selectedPickupTime = Calendar.getInstance()

    private var selectedFood: FoodItem? = null
    private var foodId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_reservation)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.reservationMain)
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

        val foodImage =
            findViewById<ImageView>(
                R.id.reservationFoodImage
            )

        val foodTitle =
            findViewById<TextView>(
                R.id.reservationFoodTitle
            )

        val category =
            findViewById<TextView>(
                R.id.reservationCategory
            )

        val quantity =
            findViewById<TextView>(
                R.id.reservationQuantity
            )

        val area =
            findViewById<TextView>(
                R.id.reservationArea
            )

        val address =
            findViewById<TextView>(
                R.id.reservationAddress
            )

        val bestBefore =
            findViewById<TextView>(
                R.id.reservationBestBefore
            )

        val safetyNotes =
            findViewById<TextView>(
                R.id.reservationSafetyNotes
            )

        pickupDateTimeEditText =
            findViewById(
                R.id.pickupDateTimeEditText
            )

        val confirmButton =
            findViewById<MaterialButton>(
                R.id.confirmReservationButton
            )

        val cancelButton =
            findViewById<MaterialButton>(
                R.id.cancelReservationButton
            )

        backContainer.setOnClickListener {
            finish()
        }

        foodId =
            intent.getIntExtra(
                "foodId",
                0
            )

        selectedFood =
            RescueData.findFoodById(
                foodId
            )

        if (selectedFood == null) {

            Toast.makeText(
                this,
                "Food listing could not be found",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        val food = selectedFood!!

        foodTitle.text =
            food.title

        category.text =
            food.category

        quantity.text =
            "${food.quantity} portions"

        area.text =
            food.area

        address.text =
            food.address

        bestBefore.text =
            "Best before ${food.bestBefore}"

        safetyNotes.text =
            food.safetyNotes

        FoodImageLoader.load(
            foodImage,
            food.imageUri
        )

        pickupDateTimeEditText.setOnClickListener {
            showDatePicker()
        }

        confirmButton.setOnClickListener {

            if (
                pickupDateTimeEditText.text
                    .toString()
                    .trim()
                    .isEmpty()
            ) {

                pickupDateTimeEditText.error =
                    "Select a pickup date and time"

                return@setOnClickListener
            }

            if (food.status != "Available") {

                Toast.makeText(
                    this,
                    "This food is no longer available",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (!pickupIsValid(food)) {

                Toast.makeText(
                    this,
                    "Pickup must be before the food best before time",
                    Toast.LENGTH_LONG
                ).show()

                return@setOnClickListener
            }

            AlertDialog.Builder(this)
                .setTitle("Confirm reservation")
                .setMessage(
                    "Reserve ${food.title} for the selected pickup time?"
                )
                .setNegativeButton(
                    "Not Yet",
                    null
                )
                .setPositiveButton(
                    "Confirm"
                ) { _, _ ->

                    createReservation(food)
                }
                .show()
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun createReservation(
        food: FoodItem
    ) {

        val reservation =
            Reservation(
                id =
                    RescueData.nextReservationId(),

                foodId =
                    food.id,

                foodTitle =
                    food.title,

                pickupDateTime =
                    pickupDateTimeEditText.text
                        .toString()
                        .trim(),

                status =
                    "Scheduled"
            )

        RescueData.addReservation(
            this,
            reservation
        )

        food.status =
            "Reserved"

        RescueData.saveData(this)

        Toast.makeText(
            this,
            "Reservation confirmed",
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }

    private fun pickupIsValid(
        food: FoodItem
    ): Boolean {

        return try {

            val formatter =
                SimpleDateFormat(
                    "dd MMM yyyy, h:mm a",
                    Locale.getDefault()
                )

            formatter.isLenient = false

            val bestBeforeDate =
                formatter.parse(
                    food.bestBefore
                )

            bestBeforeDate == null ||
                    selectedPickupTime.time
                        .before(bestBeforeDate) ||
                    selectedPickupTime.time ==
                    bestBeforeDate

        } catch (_: Exception) {

            true
        }
    }

    private fun showDatePicker() {

        val now =
            Calendar.getInstance()

        DatePickerDialog(
            this,
            { _, year, month, day ->

                selectedPickupTime.set(
                    Calendar.YEAR,
                    year
                )

                selectedPickupTime.set(
                    Calendar.MONTH,
                    month
                )

                selectedPickupTime.set(
                    Calendar.DAY_OF_MONTH,
                    day
                )

                showTimePicker()
            },

            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)

        ).apply {

            datePicker.minDate =
                System.currentTimeMillis()

        }.show()
    }

    private fun showTimePicker() {

        val now =
            Calendar.getInstance()

        TimePickerDialog(
            this,
            { _, hour, minute ->

                selectedPickupTime.set(
                    Calendar.HOUR_OF_DAY,
                    hour
                )

                selectedPickupTime.set(
                    Calendar.MINUTE,
                    minute
                )

                selectedPickupTime.set(
                    Calendar.SECOND,
                    0
                )

                updatePickupField()
            },

            now.get(
                Calendar.HOUR_OF_DAY
            ),

            now.get(
                Calendar.MINUTE
            ),

            false

        ).show()
    }

    private fun updatePickupField() {

        val formatter =
            SimpleDateFormat(
                "dd MMM yyyy, h:mm a",
                Locale.getDefault()
            )

        pickupDateTimeEditText.setText(
            formatter.format(
                selectedPickupTime.time
            )
        )
    }
}
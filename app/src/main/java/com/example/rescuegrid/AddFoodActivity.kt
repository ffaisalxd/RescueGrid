package com.example.rescuegrid

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddFoodActivity : AppCompatActivity() {

    private lateinit var bestBeforeEditText: TextInputEditText
    private lateinit var foodImagePreview: ImageView
    private lateinit var imagePlaceholderText: TextView

    private val selectedDateTime =
        Calendar.getInstance()

    private var selectedImageUri =
        ""

    private var editFoodId =
        -1

    private var editingFood:
            FoodItem? = null

    private val imagePicker =
        registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri ->

            if (uri != null) {

                try {

                    contentResolver
                        .takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )

                } catch (_: Exception) {
                }

                selectedImageUri =
                    uri.toString()

                foodImagePreview.imageTintList =
                    null

                foodImagePreview.clearColorFilter()

                foodImagePreview.setPadding(
                    0,
                    0,
                    0,
                    0
                )

                foodImagePreview.setImageURI(
                    uri
                )

                foodImagePreview.scaleType =
                    ImageView.ScaleType.CENTER_CROP

                imagePlaceholderText.text =
                    "Change Food Photo"
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(
            R.layout.activity_add_food
        )

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.addFoodMain)
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

        val titleEditText =
            findViewById<TextInputEditText>(
                R.id.foodTitleEditText
            )

        val categoryDropdown =
            findViewById<AutoCompleteTextView>(
                R.id.categoryDropdown
            )

        val quantityEditText =
            findViewById<TextInputEditText>(
                R.id.quantityEditText
            )

        val areaEditText =
            findViewById<TextInputEditText>(
                R.id.areaEditText
            )

        val addressEditText =
            findViewById<TextInputEditText>(
                R.id.addressEditText
            )

        bestBeforeEditText =
            findViewById(
                R.id.bestBeforeEditText
            )

        val safetyNotesEditText =
            findViewById<TextInputEditText>(
                R.id.safetyNotesEditText
            )

        foodImagePreview =
            findViewById(
                R.id.foodImagePreview
            )

        imagePlaceholderText =
            findViewById(
                R.id.imagePlaceholderText
            )

        val selectImageButton =
            findViewById<MaterialButton>(
                R.id.selectImageButton
            )

        val submitButton =
            findViewById<MaterialButton>(
                R.id.submitFoodButton
            )

        val cancelButton =
            findViewById<MaterialButton>(
                R.id.cancelFoodButton
            )

        backContainer.setOnClickListener {
            finish()
        }

        setupCategoryDropdown(
            categoryDropdown
        )

        editFoodId =
            intent.getIntExtra(
                "editFoodId",
                -1
            )

        if (editFoodId != -1) {

            editingFood =
                RescueData.findFoodById(
                    editFoodId
                )

            editingFood?.let { food ->

                titleEditText.setText(
                    food.title
                )

                categoryDropdown.setText(
                    food.category,
                    false
                )

                quantityEditText.setText(
                    food.quantity.toString()
                )

                areaEditText.setText(
                    food.area
                )

                addressEditText.setText(
                    food.address
                )

                bestBeforeEditText.setText(
                    food.bestBefore
                )

                safetyNotesEditText.setText(
                    food.safetyNotes
                )

                selectedImageUri =
                    food.imageUri

                submitButton.text =
                    "Save Changes"

                imagePlaceholderText.text =
                    "Change Food Photo"

                FoodImageLoader.load(
                    foodImagePreview,
                    food.imageUri
                )
            }
        }

        selectImageButton.setOnClickListener {

            imagePicker.launch(
                arrayOf("image/*")
            )
        }

        foodImagePreview.setOnClickListener {

            imagePicker.launch(
                arrayOf("image/*")
            )
        }

        bestBeforeEditText.setOnClickListener {
            showDatePicker()
        }

        submitButton.setOnClickListener {

            val title =
                titleEditText.text
                    .toString()
                    .trim()

            val category =
                categoryDropdown.text
                    .toString()
                    .trim()

            val quantityText =
                quantityEditText.text
                    .toString()
                    .trim()

            val area =
                areaEditText.text
                    .toString()
                    .trim()

            val address =
                addressEditText.text
                    .toString()
                    .trim()

            val bestBefore =
                bestBeforeEditText.text
                    .toString()
                    .trim()

            val safetyNotes =
                safetyNotesEditText.text
                    .toString()
                    .trim()

            if (title.isEmpty()) {

                titleEditText.error =
                    "Enter the food name"

                return@setOnClickListener
            }

            if (category.isEmpty()) {

                categoryDropdown.error =
                    "Select a category"

                return@setOnClickListener
            }

            if (quantityText.isEmpty()) {

                quantityEditText.error =
                    "Enter the quantity"

                return@setOnClickListener
            }

            val quantity =
                quantityText.toIntOrNull()

            if (
                quantity == null ||
                quantity <= 0
            ) {

                quantityEditText.error =
                    "Enter a valid quantity"

                return@setOnClickListener
            }

            if (area.isEmpty()) {

                areaEditText.error =
                    "Enter the collection area"

                return@setOnClickListener
            }

            if (address.isEmpty()) {

                addressEditText.error =
                    "Enter the collection address"

                return@setOnClickListener
            }

            if (bestBefore.isEmpty()) {

                bestBeforeEditText.error =
                    "Select the best before time"

                return@setOnClickListener
            }

            val existingFood =
                editingFood

            if (existingFood != null) {

                existingFood.title =
                    title

                existingFood.category =
                    category

                existingFood.quantity =
                    quantity

                existingFood.area =
                    area

                existingFood.address =
                    address

                existingFood.bestBefore =
                    bestBefore

                existingFood.safetyNotes =
                    safetyNotes.ifEmpty {
                        "No additional safety notes"
                    }

                existingFood.imageUri =
                    selectedImageUri

                RescueData.saveData(
                    this
                )

                Toast.makeText(
                    this,
                    "Listing updated",
                    Toast.LENGTH_SHORT
                ).show()

                finish()

                return@setOnClickListener
            }

            val foodItem =
                FoodItem(
                    id =
                        RescueData.nextFoodId(),

                    title =
                        title,

                    category =
                        category,

                    quantity =
                        quantity,

                    area =
                        area,

                    address =
                        address,

                    bestBefore =
                        bestBefore,

                    safetyNotes =
                        safetyNotes.ifEmpty {
                            "No additional safety notes"
                        },

                    imageUri =
                        selectedImageUri,

                    status =
                        "Available"
                )

            RescueData.addFood(
                this,
                foodItem
            )

            Toast.makeText(
                this,
                "Food listing published",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun setupCategoryDropdown(
        categoryDropdown:
        AutoCompleteTextView
    ) {

        val categories =
            listOf(
                "Bakery",
                "Produce",
                "Prepared Meals",
                "Dairy",
                "Dry Goods",
                "Beverages",
                "Other"
            )

        val adapter =
            ArrayAdapter(
                this,
                android.R.layout
                    .simple_dropdown_item_1line,
                categories
            )

        categoryDropdown.setAdapter(
            adapter
        )
    }


    private fun showDatePicker() {

        val calendar =
            Calendar.getInstance()

        DatePickerDialog(
            this,
            { _, year, month, day ->

                selectedDateTime.set(
                    Calendar.YEAR,
                    year
                )

                selectedDateTime.set(
                    Calendar.MONTH,
                    month
                )

                selectedDateTime.set(
                    Calendar.DAY_OF_MONTH,
                    day
                )

                showTimePicker()
            },

            calendar.get(
                Calendar.YEAR
            ),

            calendar.get(
                Calendar.MONTH
            ),

            calendar.get(
                Calendar.DAY_OF_MONTH
            )

        ).apply {

            datePicker.minDate =
                System.currentTimeMillis()

        }.show()
    }

    private fun showTimePicker() {

        val calendar =
            Calendar.getInstance()

        TimePickerDialog(
            this,
            { _, hour, minute ->

                selectedDateTime.set(
                    Calendar.HOUR_OF_DAY,
                    hour
                )

                selectedDateTime.set(
                    Calendar.MINUTE,
                    minute
                )

                updateBestBeforeField()
            },

            calendar.get(
                Calendar.HOUR_OF_DAY
            ),

            calendar.get(
                Calendar.MINUTE
            ),

            false

        ).show()
    }

    private fun updateBestBeforeField() {

        val formatter =
            SimpleDateFormat(
                "dd MMM yyyy, h:mm a",
                Locale.getDefault()
            )

        bestBeforeEditText.setText(
            formatter.format(
                selectedDateTime.time
            )
        )
    }
}
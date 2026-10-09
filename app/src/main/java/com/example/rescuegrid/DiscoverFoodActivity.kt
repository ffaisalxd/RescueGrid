package com.example.rescuegrid

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText

class DiscoverFoodActivity : AppCompatActivity() {

    private lateinit var foodContainer: LinearLayout
    private lateinit var emptyText: TextView
    private lateinit var resultCountText: TextView
    private lateinit var searchEditText: TextInputEditText
    private lateinit var categoryDropdown: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_discover_food)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.discoverFoodMain)
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
                R.id.discoverFoodContainer
            )

        emptyText =
            findViewById(
                R.id.discoverEmptyText
            )

        resultCountText =
            findViewById(
                R.id.resultCountText
            )

        searchEditText =
            findViewById(
                R.id.searchFoodEditText
            )

        categoryDropdown =
            findViewById(
                R.id.filterCategoryDropdown
            )

        backContainer.setOnClickListener {
            finish()
        }

        setupCategoryFilter()

        searchEditText.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    displayFilteredFood()
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )

        categoryDropdown.setOnItemClickListener { _, _, _, _ ->
            displayFilteredFood()
        }

        displayFilteredFood()
    }

    override fun onResume() {
        super.onResume()
        displayFilteredFood()
    }

    private fun setupCategoryFilter() {

        val categories =
            listOf(
                "All Categories",
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

        categoryDropdown.setAdapter(adapter)

        categoryDropdown.setText(
            "All Categories",
            false
        )
    }

    private fun displayFilteredFood() {

        foodContainer.removeAllViews()

        val searchQuery =
            searchEditText.text
                .toString()
                .trim()
                .lowercase()

        val selectedCategory =
            categoryDropdown.text
                .toString()
                .trim()

        val filteredFood =
            RescueData.foodList.filter { food ->

                val available =
                    food.status == "Available"

                val matchesSearch =
                    food.title
                        .lowercase()
                        .contains(searchQuery) ||
                            food.area
                                .lowercase()
                                .contains(searchQuery) ||
                            food.category
                                .lowercase()
                                .contains(searchQuery)

                val matchesCategory =
                    selectedCategory ==
                            "All Categories" ||
                            food.category ==
                            selectedCategory

                available &&
                        matchesSearch &&
                        matchesCategory
            }

        resultCountText.text =
            "${filteredFood.size} available"

        if (filteredFood.isEmpty()) {

            emptyText.visibility =
                View.VISIBLE

            return
        }

        emptyText.visibility =
            View.GONE

        for (food in filteredFood.reversed()) {

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
                "Available"

            FoodImageLoader.load(
                image,
                food.imageUri
            )

            card.setOnClickListener {

                val intent =
                    Intent(
                        this,
                        ReservationActivity::class.java
                    )

                intent.putExtra(
                    "foodId",
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
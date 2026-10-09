package com.example.rescuegrid

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object RescueData {

    private const val PREFS_NAME =
        "rescuegrid_prefs"

    private const val FOOD_KEY =
        "food_list"

    private const val RESERVATION_KEY =
        "reservation_list"

    private const val DEFAULT_FOOD_COUNT =
        3

    private const val STARTING_IMPACT =
        12

    val foodList =
        mutableListOf<FoodItem>()

    val reservationList =
        mutableListOf<Reservation>()

    private var initialized =
        false

    fun initialize(
        context: Context
    ) {

        if (initialized) {
            return
        }

        val preferences =
            context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )

        val savedFood =
            preferences.getString(
                FOOD_KEY,
                null
            )

        val savedReservations =
            preferences.getString(
                RESERVATION_KEY,
                null
            )

        if (savedFood.isNullOrEmpty()) {

            loadDefaultFood(
                context
            )

        } else {

            loadFoodFromJson(
                context,
                savedFood
            )
        }

        if (
            !savedReservations
                .isNullOrEmpty()
        ) {

            loadReservationsFromJson(
                savedReservations
            )
        }

        initialized = true

        saveData(
            context
        )
    }

    private fun resourceUri(
        context: Context,
        resourceId: Int
    ): String {

        return "android.resource://" +
                context.packageName +
                "/" +
                resourceId
    }

    private fun defaultImageForId(
        context: Context,
        foodId: Int
    ): String {

        return when (foodId) {

            1 ->
                resourceUri(
                    context,
                    R.drawable.bakery
                )

            2 ->
                resourceUri(
                    context,
                    R.drawable.produce
                )

            3 ->
                resourceUri(
                    context,
                    R.drawable.meals
                )

            else ->
                ""
        }
    }

    private fun loadDefaultFood(
        context: Context
    ) {

        foodList.clear()

        foodList.add(
            FoodItem(
                id = 1,

                title =
                    "Fresh Bread",

                category =
                    "Bakery",

                quantity =
                    10,

                area =
                    "Ampang",

                address =
                    "Ampang Point",

                bestBefore =
                    "26 Sep 2026, 6:00 PM",

                safetyNotes =
                    "Packed and stored at room temperature",

                imageUri =
                    defaultImageForId(
                        context,
                        1
                    ),

                status =
                    "Available"
            )
        )

        foodList.add(
            FoodItem(
                id = 2,

                title =
                    "Vegetable Packs",

                category =
                    "Produce",

                quantity =
                    6,

                area =
                    "Cheras",

                address =
                    "Taman Connaught",

                bestBefore =
                    "26 Sep 2026, 4:00 PM",

                safetyNotes =
                    "Keep refrigerated",

                imageUri =
                    defaultImageForId(
                        context,
                        2
                    ),

                status =
                    "Available"
            )
        )

        foodList.add(
            FoodItem(
                id = 3,

                title =
                    "Rice Meal Packs",

                category =
                    "Prepared Meals",

                quantity =
                    8,

                area =
                    "Kuala Lumpur",

                address =
                    "Bukit Bintang",

                bestBefore =
                    "26 Sep 2026, 8:00 PM",

                safetyNotes =
                    "Consume on the same day",

                imageUri =
                    defaultImageForId(
                        context,
                        3
                    ),

                status =
                    "Available"
            )
        )
    }

    fun addFood(
        context: Context,
        food: FoodItem
    ) {

        foodList.add(
            food
        )

        saveData(
            context
        )
    }

    fun addReservation(
        context: Context,
        reservation: Reservation
    ) {

        reservationList.add(
            reservation
        )

        saveData(
            context
        )
    }

    fun saveData(
        context: Context
    ) {

        val preferences =
            context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )

        val foodArray =
            JSONArray()

        for (
        food in foodList
        ) {

            val foodObject =
                JSONObject()

            foodObject.put(
                "id",
                food.id
            )

            foodObject.put(
                "title",
                food.title
            )

            foodObject.put(
                "category",
                food.category
            )

            foodObject.put(
                "quantity",
                food.quantity
            )

            foodObject.put(
                "area",
                food.area
            )

            foodObject.put(
                "address",
                food.address
            )

            foodObject.put(
                "bestBefore",
                food.bestBefore
            )

            foodObject.put(
                "safetyNotes",
                food.safetyNotes
            )

            foodObject.put(
                "imageUri",
                food.imageUri
            )

            foodObject.put(
                "status",
                food.status
            )

            foodArray.put(
                foodObject
            )
        }

        val reservationArray =
            JSONArray()

        for (
        reservation in
        reservationList
        ) {

            val reservationObject =
                JSONObject()

            reservationObject.put(
                "id",
                reservation.id
            )

            reservationObject.put(
                "foodId",
                reservation.foodId
            )

            reservationObject.put(
                "foodTitle",
                reservation.foodTitle
            )

            reservationObject.put(
                "pickupDateTime",
                reservation.pickupDateTime
            )

            reservationObject.put(
                "status",
                reservation.status
            )

            reservationObject.put(
                "volunteerName",
                reservation.volunteerName
            )

            reservationArray.put(
                reservationObject
            )
        }

        preferences
            .edit()
            .putString(
                FOOD_KEY,
                foodArray.toString()
            )
            .putString(
                RESERVATION_KEY,
                reservationArray.toString()
            )
            .apply()
    }

    private fun loadFoodFromJson(
        context: Context,
        json: String
    ) {

        foodList.clear()

        try {

            val array =
                JSONArray(
                    json
                )

            for (
            index in
            0 until array.length()
            ) {

                val item =
                    array.getJSONObject(
                        index
                    )

                val id =
                    item.getInt(
                        "id"
                    )

                var imageUri =
                    item.optString(
                        "imageUri",
                        ""
                    )

                /*
                 * Only migrate listings that have
                 * no image at all.
                 *
                 * A content URI selected by the
                 * donor is NEVER overwritten.
                 */
                if (
                    imageUri.isBlank()
                ) {

                    imageUri =
                        defaultImageForId(
                            context,
                            id
                        )
                }

                foodList.add(
                    FoodItem(
                        id =
                            id,

                        title =
                            item.getString(
                                "title"
                            ),

                        category =
                            item.getString(
                                "category"
                            ),

                        quantity =
                            item.getInt(
                                "quantity"
                            ),

                        area =
                            item.getString(
                                "area"
                            ),

                        address =
                            item.getString(
                                "address"
                            ),

                        bestBefore =
                            item.getString(
                                "bestBefore"
                            ),

                        safetyNotes =
                            item.getString(
                                "safetyNotes"
                            ),

                        imageUri =
                            imageUri,

                        status =
                            item.optString(
                                "status",
                                "Available"
                            )
                    )
                )
            }

        } catch (_: Exception) {

            loadDefaultFood(
                context
            )
        }
    }

    private fun loadReservationsFromJson(
        json: String
    ) {

        reservationList.clear()

        try {

            val array =
                JSONArray(
                    json
                )

            for (
            index in
            0 until array.length()
            ) {

                val item =
                    array.getJSONObject(
                        index
                    )

                reservationList.add(
                    Reservation(
                        id =
                            item.getInt(
                                "id"
                            ),

                        foodId =
                            item.getInt(
                                "foodId"
                            ),

                        foodTitle =
                            item.getString(
                                "foodTitle"
                            ),

                        pickupDateTime =
                            item.getString(
                                "pickupDateTime"
                            ),

                        status =
                            item.getString(
                                "status"
                            ),

                        volunteerName =
                            item.optString(
                                "volunteerName",
                                ""
                            )
                    )
                )
            }

        } catch (_: Exception) {

            reservationList.clear()
        }
    }

    fun nextFoodId(): Int {

        return if (
            foodList.isEmpty()
        ) {

            1

        } else {

            foodList.maxOf {
                it.id
            } + 1
        }
    }

    fun nextReservationId(): Int {

        return if (
            reservationList.isEmpty()
        ) {

            1

        } else {

            reservationList.maxOf {
                it.id
            } + 1
        }
    }

    fun findFoodById(
        id: Int
    ): FoodItem? {

        return foodList.find {
            it.id == id
        }
    }

    fun getImpactCount(): Int {

        val addedListings =
            (
                    foodList.size -
                            DEFAULT_FOOD_COUNT
                    ).coerceAtLeast(
                    0
                )

        return STARTING_IMPACT +
                addedListings
    }

    fun getActiveListingsCount(): Int {

        return foodList.count {
            it.status !=
                    "Collected"
        }
    }

    fun getCompletedListingsCount(): Int {

        return foodList.count {
            it.status ==
                    "Collected"
        }
    }
}
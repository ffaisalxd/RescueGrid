package com.example.rescuegrid

data class FoodItem(
    val id: Int,
    var title: String,
    var category: String,
    var quantity: Int,
    var area: String,
    var address: String,
    var bestBefore: String,
    var safetyNotes: String,
    var imageUri: String = "",
    var status: String = "Available"
)
package com.example.rescuegrid

data class Reservation(
    val id: Int,
    val foodId: Int,
    val foodTitle: String,
    val pickupDateTime: String,
    var status: String = "Scheduled",
    var volunteerName: String = ""
)
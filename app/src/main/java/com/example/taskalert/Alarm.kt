package com.example.taskalert

// Alarm data class with AM/PM mode
data class Alarm(
    var id: String = "",
    val hour: Int = 0,
    val minute: Int = 0,
    /*val isAM: String = ""*/ // true for AM, false for PM
)


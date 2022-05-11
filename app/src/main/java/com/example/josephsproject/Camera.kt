package com.example.josephsproject

import org.json.JSONArray

data class Camera (
    val Id: String,
    val Description: String,
    val ImageUrl: String,
    val Type: String,
    val Coords: JSONArray
    )


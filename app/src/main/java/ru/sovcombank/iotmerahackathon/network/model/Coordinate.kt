package ru.sovcombank.iotmerahackathon.network.model

import com.google.gson.annotations.SerializedName

class Coordinate {

    @field:SerializedName("x")
    val x: Double = 0.0

    @field:SerializedName("y")
    val y: Double = 0.0
}
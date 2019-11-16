package ru.sovcombank.iotmerahackathon.entity

import com.google.gson.annotations.SerializedName

data class Location(
    @field:SerializedName("x")
    val x: Double,
    @field:SerializedName("y")
    val y: Double

)
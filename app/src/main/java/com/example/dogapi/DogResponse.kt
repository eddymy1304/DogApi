package com.example.dogapi

import com.google.gson.annotations.SerializedName

data class DogResponse(
    @SerializedName("status") var estado: String,
    @SerializedName("message") var imagenes: List<String>
)
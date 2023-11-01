package com.taufik.prayerclock.data.dto

import com.google.gson.annotations.SerializedName

data class AvailableRegencyCityResponseDto(
    val id: String?,
    @SerializedName("lokasi")
    val name: String?
)
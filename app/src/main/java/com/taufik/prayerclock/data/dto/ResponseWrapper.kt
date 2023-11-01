package com.taufik.prayerclock.data.dto

data class ResponseWrapper<T>(
    val status: Boolean?,
    val message: String?,
    val data: T?
)
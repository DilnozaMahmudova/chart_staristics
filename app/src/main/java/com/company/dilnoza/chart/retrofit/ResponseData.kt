package com.company.dilnoza.chart.retrofit

data class ResponseData<T>(
    val status: String,
    val message: String = "Successful",
    val data: T? = null
)
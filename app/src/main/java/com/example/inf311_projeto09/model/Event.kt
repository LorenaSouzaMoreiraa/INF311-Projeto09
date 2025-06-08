package com.example.inf311_projeto09.model

import java.util.Date

data class Event(
    val id: String,
    val title: String,
    val beginTime: Date,
    val endTime: Date,
    val type: String,
    val checkInEnable: Date?,
    val checkInTime: Date?,
    val checkOutEnable: Date?,
    val checkOutTime: Date?,
)
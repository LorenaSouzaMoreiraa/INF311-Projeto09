package com.example.inf311_projeto09.model

// TODO substituir timeAgo para Data
data class Notification(
    val id: Int,
    val title: String,
    val description: String,
    val timeAgo: String,
    var isRead: Boolean = false
)

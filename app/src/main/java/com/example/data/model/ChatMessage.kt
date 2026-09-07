package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val companionId: String,
    val sender: String, // "USER" or "COMPANION"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

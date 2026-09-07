package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bookingReference: String,
    val companionId: String,
    val companionName: String,
    val companionAge: Int,
    val companionPhotoRes: Int,
    val eventType: String,
    val date: String,
    val timeSlot: String,
    val durationHours: Int,
    val hourlyRate: Int,
    val baseAmount: Int,
    val platformFee: Int,
    val taxAmount: Int,
    val discountAmount: Int,
    val totalAmount: Int,
    val paymentMethod: String,
    val venueAddress: String,
    val specialNotes: String,
    val status: String = "CONFIRMED", // CONFIRMED, IN_PROGRESS, COMPLETED
    val createdAt: Long = System.currentTimeMillis()
)

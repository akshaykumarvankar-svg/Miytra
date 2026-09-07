package com.example.data.model

import androidx.annotation.DrawableRes
import com.example.R

enum class CompanionGender(val displayName: String) {
    GIRL("Girl"),
    BOY("Boy"),
    ANY("Any")
}

enum class EventCategory(val id: String, val title: String, val emoji: String) {
    ALL("all", "All Events", "✨"),
    DINNER_DATE("dinner", "Dinner Dates", "🍷"),
    MOVIE_PARTNER("movie", "Movie Partners", "🎬"),
    EVENT_COMPANION("event", "Event Companions", "🎭"),
    PARTY_PARTNER("party", "Party Partners", "🪩"),
    CASUAL_HANGOUT("hangout", "Casual Hangouts", "☕")
}

data class Review(
    val author: String,
    val rating: Float,
    val date: String,
    val comment: String,
    val eventType: String
)

data class VerificationInfo(
    val isGovtIdVerified: Boolean = true,
    val isBiometricChecked: Boolean = true,
    val isPoliceClearanceValid: Boolean = true,
    val isSafetyTrained: Boolean = true,
    val verificationDate: String = "Verified May 2026",
    val badgeLevel: String = "Mityra Shield Level 3"
)

data class Companion(
    val id: String,
    val name: String,
    val age: Int,
    val gender: CompanionGender,
    val city: String,
    val neighborhood: String,
    val primaryCategory: EventCategory,
    val hourlyRate: Int,
    val rating: Float,
    val reviewCount: Int,
    val bio: String,
    val interests: List<String>,
    val languages: List<String>,
    val boundaries: List<String>,
    @DrawableRes val imageResId: Int,
    val isVerified: Boolean = true,
    val verificationInfo: VerificationInfo = VerificationInfo(),
    val reviews: List<Review> = emptyList(),
    val isAvailableToday: Boolean = true
)

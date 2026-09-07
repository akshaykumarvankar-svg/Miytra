package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole(val label: String, val subtitle: String) {
    MEMBER("Event Guest / Member", "Looking to book vetted companions for dinners, movies, & events"),
    COMPANION("Verified Companion", "Join Mityra to accompany vetted members and earn hourly fees"),
    ADMIN("Administrator", "Access platform operations, review KYC applications, and audit payments")
}

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey val id: String = "primary_user",
    val name: String,
    val role: String = "MEMBER", // "MEMBER", "COMPANION", or "ADMIN"
    val password: String = "pass123",
    val age: Int = 24,
    val gender: String = "Male", // "Male", "Female", "Non-binary"
    val city: String = "Mumbai",
    val neighborhood: String = "Bandra West",
    val phone: String = "+91 98201 44892",
    val email: String = "rohan.sharma@example.com",
    val bio: String = "Tech enthusiast who enjoys rooftop dining, art galleries, and weekend film screenings.",
    val interests: String = "Fine Dining 🍷, Modern Art 🎨, Film Screenings 🎬, Rooftop Lounges 🍸",
    val languages: String = "English, Hindi",
    val emergencyContactName: String = "Priya Sharma",
    val emergencyContactPhone: String = "+91 98201 44892",
    val safeWord: String = "SUNSHINE",
    val isKycVerified: Boolean = true,
    val kycDocumentType: String = "Aadhaar Card",
    val kycIdMasked: String = "XXXX-XXXX-8921",
    val avatarGradientStart: Long = 0xFFFF5E62,
    val avatarGradientEnd: Long = 0xFF7928CA,
    // Companion-specific fields (used when role is COMPANION)
    val primaryCategory: String = "dinner",
    val hourlyRate: Int = 750,
    val boundaries: String = "Public venues only, Strictly platonic companion, Safe travel coordination",
    val hasActiveMembership: Boolean = true,
    val membershipExpiry: Long? = System.currentTimeMillis() + 30L * 24 * 3600 * 1000,
    val membershipPaymentId: String? = "pay_RzpMityra88201",
    val companionApplicationStatus: String = "APPROVED",
    val createdAt: Long = System.currentTimeMillis()
)

package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ApplicationStatus(val label: String) {
    PENDING("Pending Review"),
    APPROVED("Approved"),
    REJECTED("Rejected")
}

@Entity(tableName = "companion_applications")
data class CompanionApplicationEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val name: String,
    val age: Int,
    val gender: String,
    val city: String,
    val neighborhood: String,
    val phone: String,
    val email: String,
    val bio: String,
    val interests: String,
    val languages: String,
    val primaryCategory: String,
    val hourlyRate: Int,
    val boundaries: String,
    val kycDocumentType: String,
    val kycIdMasked: String,
    val isKycVerified: Boolean,
    val status: String = "PENDING", // "PENDING", "APPROVED", "REJECTED"
    val adminNotes: String = "",
    val submittedAt: Long = System.currentTimeMillis(),
    val reviewedAt: Long? = null
)

@Entity(tableName = "membership_payments")
data class MembershipPaymentEntity(
    @PrimaryKey val paymentId: String, // e.g., "pay_rzp_..."
    val orderId: String, // e.g., "order_Mityra_..."
    val userId: String,
    val userName: String,
    val userPhone: String,
    val userEmail: String,
    val amount: Int = 49,
    val currency: String = "INR",
    val paymentMethod: String, // "UPI (Google Pay)", "UPI (PhonePe)", "Card", "Netbanking"
    val status: String = "SUCCESS", // "SUCCESS", "FAILED"
    val planName: String = "Mityra VIP Club (₹49/month)",
    val razorpaySignature: String,
    val timestamp: Long = System.currentTimeMillis(),
    val validUntil: Long = System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000
)

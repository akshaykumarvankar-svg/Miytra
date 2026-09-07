package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.CompanionApplicationEntity
import com.example.data.model.MembershipPaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AdminDao {

    // --- Companion Applications ---
    @Query("SELECT * FROM companion_applications ORDER BY submittedAt DESC")
    fun getAllApplications(): Flow<List<CompanionApplicationEntity>>

    @Query("SELECT * FROM companion_applications WHERE id = :id LIMIT 1")
    suspend fun getApplicationById(id: String): CompanionApplicationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(application: CompanionApplicationEntity)

    @Query("UPDATE companion_applications SET status = :status, adminNotes = :notes, reviewedAt = :reviewedAt WHERE id = :id")
    suspend fun updateApplicationStatus(id: String, status: String, notes: String, reviewedAt: Long = System.currentTimeMillis())

    // --- ₹49 Membership Payments ---
    @Query("SELECT * FROM membership_payments ORDER BY timestamp DESC")
    fun getAllPayments(): Flow<List<MembershipPaymentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: MembershipPaymentEntity)

    @Query("SELECT COUNT(*) FROM membership_payments WHERE status = 'SUCCESS'")
    fun getSuccessPaymentCount(): Flow<Int>

    @Query("SELECT * FROM membership_payments WHERE paymentId = :paymentId LIMIT 1")
    suspend fun getPaymentById(paymentId: String): MembershipPaymentEntity?
}

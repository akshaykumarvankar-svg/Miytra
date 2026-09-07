package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.model.CompanionGender
import com.example.data.model.EventCategory
import com.example.data.model.UserProfileEntity
import com.example.data.model.UserRole
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private lateinit var database: AppDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `read string from context verifies Mityra app name`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Mityra", appName)
    }

    @Test
    fun `event categories and companion genders have valid display properties`() {
        assertEquals("Dinner Dates", EventCategory.DINNER_DATE.title)
        assertEquals("Movie Partners", EventCategory.MOVIE_PARTNER.title)
        assertEquals("Event Companions", EventCategory.EVENT_COMPANION.title)
        assertEquals("Party Partners", EventCategory.PARTY_PARTNER.title)

        assertEquals("Boy", CompanionGender.BOY.displayName)
        assertEquals("Girl", CompanionGender.GIRL.displayName)
        assertEquals("Any", CompanionGender.ANY.displayName)
    }

    @Test
    fun `user profile self registration and database persistence`() = runBlocking {
        val userProfileDao = database.userProfileDao()

        val newProfile = UserProfileEntity(
            id = "test_user_1",
            name = "Rohan Sharma",
            phone = "+91 98765 43210",
            email = "rohan.sharma@example.com",
            city = "Mumbai",
            neighborhood = "Bandra West",
            bio = "Love attending indie concerts and dining at gourmet bistros.",
            interests = "Live Music, Food Tasting, Coffee, Photography",
            languages = "English, Hindi",
            role = "COMPANION",
            hourlyRate = 750,
            primaryCategory = "Event Companions",
            gender = "Male",
            age = 25,
            emergencyContactName = "Suresh Sharma",
            emergencyContactPhone = "+91 91234 56789",
            safeWord = "NEBULA",
            isKycVerified = true
        )

        userProfileDao.saveUserProfile(newProfile)

        val retrievedProfile = userProfileDao.getUserProfile("test_user_1").first()
        assertNotNull(retrievedProfile)
        assertEquals("Rohan Sharma", retrievedProfile?.name)
        assertEquals("COMPANION", retrievedProfile?.role)
        assertEquals(750, retrievedProfile?.hourlyRate)
        assertEquals("NEBULA", retrievedProfile?.safeWord)
        assertTrue(retrievedProfile?.isKycVerified == true)
    }
}

package com.example.data.repository

import com.example.R
import com.example.data.local.AdminDao
import com.example.data.local.BookingDao
import com.example.data.local.ChatDao
import com.example.data.local.UserProfileDao
import com.example.data.model.BookingEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.model.Companion
import com.example.data.model.CompanionApplicationEntity
import com.example.data.model.CompanionGender
import com.example.data.model.EventCategory
import com.example.data.model.MembershipPaymentEntity
import com.example.data.model.Review
import com.example.data.model.UserProfileEntity
import com.example.data.model.VerificationInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MityraRepository(
    private val bookingDao: BookingDao,
    private val chatDao: ChatDao,
    private val userProfileDao: UserProfileDao,
    private val adminDao: AdminDao
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _customCompanions = MutableStateFlow<List<Companion>>(emptyList())
    val customCompanions: Flow<List<Companion>> = _customCompanions.asStateFlow()

    val companionApplications: Flow<List<CompanionApplicationEntity>> = adminDao.getAllApplications()
    val membershipPayments: Flow<List<MembershipPaymentEntity>> = adminDao.getAllPayments()

    fun getAllCompanions(): List<Companion> {
        return companionsList + _customCompanions.value
    }

    fun registerCustomCompanion(companion: Companion) {
        _customCompanions.value = _customCompanions.value + companion
    }

    val companionsList: List<Companion> = listOf(
        Companion(
            id = "comp_1",
            name = "Alina Roy",
            age = 24,
            gender = CompanionGender.GIRL,
            city = "Mumbai",
            neighborhood = "Bandra West",
            primaryCategory = EventCategory.DINNER_DATE,
            hourlyRate = 750,
            rating = 4.92f,
            reviewCount = 58,
            bio = "Sommelier-in-training & lifestyle blogger. Love deep conversations over Italian pasta, rooftop cocktails, and art exhibition openings. Easygoing, engaging, and polished.",
            interests = listOf("Fine Dining 🍷", "Modern Art 🎨", "Rooftop Lounges 🍸", "Jazz Nights 🎷", "World Travel ✈️"),
            languages = listOf("English", "Hindi", "French (Conversational)"),
            boundaries = listOf("Public & licensed venues only", "Strictly social & non-romantic companion", "Safe transport coordination", "Respect personal space"),
            imageResId = R.drawable.img_comp_alina,
            isVerified = true,
            verificationInfo = VerificationInfo(
                isGovtIdVerified = true,
                isBiometricChecked = true,
                isPoliceClearanceValid = true,
                isSafetyTrained = true,
                verificationDate = "Verified April 2026",
                badgeLevel = "Mityra Shield Level 3 (Elite)"
            ),
            reviews = listOf(
                Review("Aditya K.", 5.0f, "Yesterday", "Alina was the perfect companion for our client dinner at Bandra. Incredibly articulate and classy!", "Dinner Date"),
                Review("Rohit S.", 4.9f, "Last week", "Super conversational and made the art showcase 10x more enjoyable. Highly recommended!", "Art Event")
            )
        ),
        Companion(
            id = "comp_2",
            name = "Kabir Mehra",
            age = 26,
            gender = CompanionGender.BOY,
            city = "Mumbai",
            neighborhood = "Juhu / Andheri",
            primaryCategory = EventCategory.PARTY_PARTNER,
            hourlyRate = 650,
            rating = 4.88f,
            reviewCount = 44,
            bio = "Music producer and fitness coach. Looking for someone to vibe with at EDM festivals, techno nights, or high-energy parties? I'm your wingman and hype partner!",
            interests = listOf("Techno & EDM 🎧", "Cocktail Mixology 🍹", "VIP Lounges 🪩", "Sneakers 👟", "Fitness 🏋️"),
            languages = listOf("English", "Hindi", "Punjabi"),
            boundaries = listOf("Public club & event settings only", "No reckless activities", "Safety escorts provided", "Professional companion etiquette"),
            imageResId = R.drawable.img_comp_kabir,
            isVerified = true,
            verificationInfo = VerificationInfo(
                isGovtIdVerified = true,
                isBiometricChecked = true,
                isPoliceClearanceValid = true,
                isSafetyTrained = true,
                verificationDate = "Verified March 2026",
                badgeLevel = "Mityra Shield Level 3"
            ),
            reviews = listOf(
                Review("Meera P.", 5.0f, "3 days ago", "Attended Sunburn festival with Kabir. Felt 100% safe, energetic, and had an absolute blast dancing!", "Party Partner"),
                Review("Karan G.", 4.8f, "2 weeks ago", "Great vibe, courteous, and kept the party energy going all night.", "Club Night")
            )
        ),
        Companion(
            id = "comp_3",
            name = "Tanya Sen",
            age = 25,
            gender = CompanionGender.GIRL,
            city = "Mumbai",
            neighborhood = "Colaba / South Bombay",
            primaryCategory = EventCategory.EVENT_COMPANION,
            hourlyRate = 900,
            rating = 4.96f,
            reviewCount = 76,
            bio = "Literature graduate & theater enthusiast. Ideal companion for corporate galas, literary festivals, heritage walks, and high-profile networking events.",
            interests = listOf("Gala Dinners 💎", "Theater & Plays 🎭", "Philosophy 📚", "Classical Music 🎻", "Heritage Walks 🏛️"),
            languages = listOf("English", "Hindi", "Bengali"),
            boundaries = listOf("Formal / social occasions only", "Zero tolerance for harassment", "Pre-agreed timings and location"),
            imageResId = R.drawable.img_comp_tanya,
            isVerified = true,
            verificationInfo = VerificationInfo(
                isGovtIdVerified = true,
                isBiometricChecked = true,
                isPoliceClearanceValid = true,
                isSafetyTrained = true,
                verificationDate = "Verified February 2026",
                badgeLevel = "Mityra Shield Level 3 (Gold)"
            ),
            reviews = listOf(
                Review("Vikram M.", 5.0f, "5 days ago", "Accompanied me to a high-profile business gala. Tanya is phenomenal, brilliant conversationalist!", "Gala Event"),
                Review("Shalini T.", 4.9f, "1 month ago", "Attended the literature festival together. Such deep insight and warm warmth.", "Cultural Event")
            )
        ),
        Companion(
            id = "comp_4",
            name = "Samira Khan",
            age = 23,
            gender = CompanionGender.GIRL,
            city = "Bengaluru",
            neighborhood = "Indiranagar",
            primaryCategory = EventCategory.MOVIE_PARTNER,
            hourlyRate = 500,
            rating = 4.85f,
            reviewCount = 38,
            bio = "Cinephile & coffee roaster nerd. Catching late-night IMAX premieres, discussing film easter eggs, or playing board games over pour-over brew.",
            interests = listOf("IMAX & Sci-Fi 🍿", "Indie Cinema 🎬", "Specialty Coffee ☕", "Board Games 🎲", "Anime 🌸"),
            languages = listOf("English", "Hindi", "Kannada"),
            boundaries = listOf("Theaters and public cafes only", "Friendly platonic outing", "Confirmed ticket reservation"),
            imageResId = R.drawable.img_comp_alina, // reuse styled photo
            isVerified = true,
            verificationInfo = VerificationInfo(
                isGovtIdVerified = true,
                isBiometricChecked = true,
                isPoliceClearanceValid = true,
                isSafetyTrained = true,
                verificationDate = "Verified May 2026",
                badgeLevel = "Mityra Shield Level 2"
            ),
            reviews = listOf(
                Review("Deepak R.", 5.0f, "1 week ago", "Watched Oppenheimer together. Her movie trivia made the experience fantastic!", "Movie Night")
            )
        ),
        Companion(
            id = "comp_5",
            name = "Rohan Varma",
            age = 27,
            gender = CompanionGender.BOY,
            city = "Delhi NCR",
            neighborhood = "Gurgaon CyberHub",
            primaryCategory = EventCategory.DINNER_DATE,
            hourlyRate = 800,
            rating = 4.91f,
            reviewCount = 52,
            bio = "Architect and wine lover. Sophisticated dinner companion for fine dining, golf club brunches, and live jazz sessions. Respectful, well-traveled, and courteous.",
            interests = listOf("Wine Tasting 🍷", "Architecture 🏛️", "Golf ⛳", "Bespoke Suiting 👔", "Acoustic Live Music 🎸"),
            languages = listOf("English", "Hindi"),
            boundaries = listOf("Public dining establishments", "Zero intoxication policy", "Punctual schedules"),
            imageResId = R.drawable.img_comp_kabir,
            isVerified = true,
            verificationInfo = VerificationInfo(
                isGovtIdVerified = true,
                isBiometricChecked = true,
                isPoliceClearanceValid = true,
                isSafetyTrained = true,
                verificationDate = "Verified January 2026",
                badgeLevel = "Mityra Shield Level 3 (Elite)"
            ),
            reviews = listOf(
                Review("Ananya D.", 5.0f, "4 days ago", "Rohan was gentlemanly, knowledgeable about Italian wines, and made my birthday dinner memorable!", "Dinner Date")
            )
        ),
        Companion(
            id = "comp_6",
            name = "Maya Patel",
            age = 24,
            gender = CompanionGender.GIRL,
            city = "Pune",
            neighborhood = "Koregaon Park",
            primaryCategory = EventCategory.CASUAL_HANGOUT,
            hourlyRate = 550,
            rating = 4.79f,
            reviewCount = 29,
            bio = "Graphic designer and thrift shop explorer. Need a friendly buddy to visit weekend flea markets, botanical gardens, or discover cozy hidden cafes?",
            interests = listOf("Flea Markets 🛍️", "Live Acoustic 🎵", "Pottery 🏺", "City Walks 🌿", "Cats & Dogs 🐾"),
            languages = listOf("English", "Hindi", "Marathi"),
            boundaries = listOf("Daytime or well-lit public spots", "Non-intimate social engagement", "Mutual respect"),
            imageResId = R.drawable.img_comp_tanya,
            isVerified = true,
            verificationInfo = VerificationInfo(
                isGovtIdVerified = true,
                isBiometricChecked = true,
                isPoliceClearanceValid = true,
                isSafetyTrained = true,
                verificationDate = "Verified April 2026",
                badgeLevel = "Mityra Shield Level 2"
            ),
            reviews = listOf(
                Review("Siddharth N.", 4.8f, "2 weeks ago", "Maya is a super warm person, showed me amazing heritage bakeries in KP!", "Casual Hangout")
            )
        )
    )

    fun getCompanionById(id: String): Companion {
        return companionsList.find { it.id == id } ?: companionsList.first()
    }

    fun getAllBookings(): Flow<List<BookingEntity>> = bookingDao.getAllBookings()

    suspend fun saveBooking(booking: BookingEntity): Long {
        val newId = bookingDao.insertBooking(booking)
        // Also seed an initial chat confirmation message
        chatDao.insertMessage(
            ChatMessageEntity(
                companionId = booking.companionId,
                sender = "COMPANION",
                text = "Hi! I just saw your booking for ${booking.eventType} on ${booking.date} at ${booking.timeSlot}. Looking forward to a great time together! Let me know if you have any special requests. ✨",
                timestamp = System.currentTimeMillis()
            )
        )
        return newId
    }

    suspend fun updateBookingStatus(id: Long, status: String) {
        bookingDao.updateBookingStatus(id, status)
    }

    fun getChatMessages(companionId: String): Flow<List<ChatMessageEntity>> {
        return chatDao.getMessagesForCompanion(companionId)
    }

    suspend fun sendMessage(companionId: String, text: String) {
        // Insert user's message
        chatDao.insertMessage(
            ChatMessageEntity(
                companionId = companionId,
                sender = "USER",
                text = text,
                timestamp = System.currentTimeMillis()
            )
        )

        // Simulate intelligent companion reply after short delay
        scope.launch {
            delay(1200)
            val companion = getCompanionById(companionId)
            val replyText = generateCompanionReply(companion, text)
            chatDao.insertMessage(
                ChatMessageEntity(
                    companionId = companionId,
                    sender = "COMPANION",
                    text = replyText,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    private fun generateCompanionReply(companion: Companion, userMsg: String): String {
        val lower = userMsg.lowercase()
        return when {
            lower.contains("cuisine") || lower.contains("food") || lower.contains("eat") || lower.contains("restaurant") ->
                "Italian or Asian fusion sounds wonderful! I know a fantastic spot with lovely ambient lighting. What vibe do you prefer? 🍷"
            lower.contains("wear") || lower.contains("dress") || lower.contains("outfit") ->
                "Smart chic or semi-formal works best! I'll match whatever dress code you decide for the venue. 👗✨"
            lower.contains("time") || lower.contains("reach") || lower.contains("meet") ->
                "I'm very punctual and will reach the venue 10 minutes prior. You'll see me near the reception or main entrance! 🕒"
            lower.contains("music") || lower.contains("song") || lower.contains("band") ->
                "I love soulful acoustic, deep house, and jazz! What's currently on your top playlist? 🎶"
            lower.contains("safety") || lower.contains("safe") || lower.contains("verification") ->
                "Safety is Mityra's #1 rule! All our outings have active GPS tracking and SOS support for both of us. Looking forward to meeting you! 🛡️"
            else ->
                "Sounds great! Thanks for letting me know. Feel free to confirm the booking time slot whenever you're ready, and I'll block my calendar! 😊"
        }
    }

    suspend fun seedInitialDataIfEmpty() {
        // Check if bookings are empty, seed 1 sample active booking
        val currentBookings = bookingDao.getAllBookings().first()
        if (currentBookings.isEmpty()) {
            val sampleCompanion = companionsList[0] // Alina Roy
            val sampleBooking = BookingEntity(
                bookingReference = "MIT-92841",
                companionId = sampleCompanion.id,
                companionName = sampleCompanion.name,
                companionAge = sampleCompanion.age,
                companionPhotoRes = sampleCompanion.imageResId,
                eventType = "Dinner Dates",
                date = "Tonight, 8:00 PM",
                timeSlot = "08:00 PM - 11:00 PM",
                durationHours = 3,
                hourlyRate = sampleCompanion.hourlyRate,
                baseAmount = sampleCompanion.hourlyRate * 3,
                platformFee = 99,
                taxAmount = 145,
                discountAmount = 150,
                totalAmount = (sampleCompanion.hourlyRate * 3) + 99 + 145 - 150,
                paymentMethod = "UPI (Google Pay)",
                venueAddress = "Bastian at the Top, 48th Floor, Kohinoor Square, Dadar, Mumbai",
                specialNotes = "Table booked under reservation name 'Akshay'. Smart casual dress code.",
                status = "CONFIRMED",
                createdAt = System.currentTimeMillis() - 3600000
            )
            bookingDao.insertBooking(sampleBooking)

            // Seed initial chat with Alina
            chatDao.insertMessage(
                ChatMessageEntity(
                    companionId = sampleCompanion.id,
                    sender = "COMPANION",
                    text = "Hello! 👋 I saw your reservation request for Bastian tonight. Looking forward to our dinner date! Have you been there before?",
                    timestamp = System.currentTimeMillis() - 3500000
                )
            )
            chatDao.insertMessage(
                ChatMessageEntity(
                    companionId = sampleCompanion.id,
                    sender = "USER",
                    text = "Hey Alina! First time there actually, heard great things about the view.",
                    timestamp = System.currentTimeMillis() - 3200000
                )
            )
            chatDao.insertMessage(
                ChatMessageEntity(
                    companionId = sampleCompanion.id,
                    sender = "COMPANION",
                    text = "The sunset skyline is unbelievable! I'll be there right at 8:00 PM in smart-chic attire. See you soon! ✨",
                    timestamp = System.currentTimeMillis() - 2800000
                )
            )
        }

        val currentProfile = userProfileDao.getUserProfileOnce()
        if (currentProfile == null) {
            userProfileDao.saveUserProfile(
                UserProfileEntity(
                    id = "primary_user",
                    name = "Rohan Sharma",
                    role = "MEMBER",
                    age = 25,
                    gender = "Male",
                    city = "Mumbai",
                    neighborhood = "Bandra West",
                    phone = "+91 98201 44892",
                    email = "rohan.sharma@example.com",
                    bio = "Tech founder & lifestyle enthusiast. Love rooftop dinners, art gallery openings, and weekend indie film screenings.",
                    interests = "Fine Dining 🍷, Modern Art 🎨, Film Screenings 🎬, Rooftop Lounges 🍸",
                    languages = "English, Hindi, Marathi",
                    emergencyContactName = "Priya Sharma",
                    emergencyContactPhone = "+91 98201 44892",
                    safeWord = "SUNSHINE",
                    isKycVerified = true,
                    kycDocumentType = "Aadhaar Card",
                    kycIdMasked = "XXXX-XXXX-8921",
                    avatarGradientStart = 0xFFFF5E62,
                    avatarGradientEnd = 0xFF7928CA,
                    hasActiveMembership = false
                )
            )
        }

        // Initialize sample companion applications for admin console
        val existingApps = adminDao.getAllApplications().firstOrNull() ?: emptyList()
        if (existingApps.isEmpty()) {
            adminDao.insertApplication(
                CompanionApplicationEntity(
                    id = "app_101",
                    userId = "usr_aarav_21",
                    name = "Aarav Kapoor",
                    age = 25,
                    gender = "Boy",
                    city = "Bengaluru",
                    neighborhood = "Koramangala 4th Block",
                    phone = "+91 98450 11234",
                    email = "aarav.kapoor@gmail.com",
                    bio = "Standup comedy fan, foodie explorer, and board games enthusiast. Love hosting casual cafe meetups and dinner companionships.",
                    interests = "Standup Comedy 🎤, Board Games 🎲, Coffee Tasting ☕, Pub Quiz 🧠",
                    languages = "English, Hindi, Kannada",
                    primaryCategory = "Casual Hangouts",
                    hourlyRate = 600,
                    boundaries = "Strictly platonic, Public venues only, No alcohol-heavy environments",
                    kycDocumentType = "Aadhaar Card",
                    kycIdMasked = "XXXX-XXXX-4519",
                    isKycVerified = true,
                    status = "PENDING",
                    adminNotes = "Aadhaar biometric verified. Police check pending verification.",
                    submittedAt = System.currentTimeMillis() - 7200000
                )
            )
            adminDao.insertApplication(
                CompanionApplicationEntity(
                    id = "app_102",
                    userId = "usr_sanya_33",
                    name = "Sanya Malhotra",
                    age = 23,
                    gender = "Girl",
                    city = "Delhi NCR",
                    neighborhood = "Hauz Khas Village",
                    phone = "+91 98110 99882",
                    email = "sanya.m@outlook.com",
                    bio = "Classical dancer & cultural guide. Available for historical monument tours, theater nights, and curated art gallery events.",
                    interests = "Classical Dance 💃, Heritage Walks 🏛️, Modern Art 🎨, Literary Festivals 📖",
                    languages = "English, Hindi",
                    primaryCategory = "Event Companions",
                    hourlyRate = 850,
                    boundaries = "Public heritage & cultural venues only. Formal pre-booking required.",
                    kycDocumentType = "Passport",
                    kycIdMasked = "ZXXXXXX41",
                    isKycVerified = true,
                    status = "PENDING",
                    adminNotes = "Passport photo matches selfie. Ready for final partner approval.",
                    submittedAt = System.currentTimeMillis() - 14400000
                )
            )
            adminDao.insertApplication(
                CompanionApplicationEntity(
                    id = "app_103",
                    userId = "usr_vikram_88",
                    name = "Vikram Singhania",
                    age = 28,
                    gender = "Boy",
                    city = "Mumbai",
                    neighborhood = "Lower Parel",
                    phone = "+91 99201 55667",
                    email = "vikram.s@singhania.io",
                    bio = "Architect and jazz enthusiast. Sophisticated dinner companion for fine dining and rooftop networking gatherings.",
                    interests = "Fine Dining 🍷, Architecture 🏛️, Jazz & Vinyl 🎷, Cigar Lounge 🍸",
                    languages = "English, Hindi, German",
                    primaryCategory = "Dinner Dates",
                    hourlyRate = 1100,
                    boundaries = "Five-star hotel lounges & premium bistros only.",
                    kycDocumentType = "Driving License",
                    kycIdMasked = "MH-01-XXXX-9912",
                    isKycVerified = true,
                    status = "APPROVED",
                    adminNotes = "VIP Host certified. Shield Level 3 verified.",
                    submittedAt = System.currentTimeMillis() - 86400000,
                    reviewedAt = System.currentTimeMillis() - 43200000
                )
            )
        }

        // Initialize sample ₹49 Razorpay membership transactions
        val existingPayments = adminDao.getAllPayments().firstOrNull() ?: emptyList()
        if (existingPayments.isEmpty()) {
            adminDao.insertPayment(
                MembershipPaymentEntity(
                    paymentId = "pay_RzpMityra99281",
                    orderId = "order_Mityra_Sub_7812",
                    userId = "usr_neha_54",
                    userName = "Neha Deshmukh",
                    userPhone = "+91 98200 44551",
                    userEmail = "neha.deshmukh@gmail.com",
                    amount = 49,
                    paymentMethod = "UPI (Google Pay)",
                    status = "SUCCESS",
                    planName = "Mityra VIP Club (₹49/mo)",
                    razorpaySignature = "rzp_sig_a78fb12948cbb9",
                    timestamp = System.currentTimeMillis() - 3600000 * 4
                )
            )
            adminDao.insertPayment(
                MembershipPaymentEntity(
                    paymentId = "pay_RzpMityra88192",
                    orderId = "order_Mityra_Sub_7813",
                    userId = "usr_aditya_12",
                    userName = "Aditya Kulkarni",
                    userPhone = "+91 99302 77881",
                    userEmail = "aditya.k@techcorp.in",
                    amount = 49,
                    paymentMethod = "Credit Card (Visa •• 4242)",
                    status = "SUCCESS",
                    planName = "Mityra VIP Club (₹49/mo)",
                    razorpaySignature = "rzp_sig_cd910248fca889",
                    timestamp = System.currentTimeMillis() - 3600000 * 18
                )
            )
            adminDao.insertPayment(
                MembershipPaymentEntity(
                    paymentId = "pay_RzpMityra77103",
                    orderId = "order_Mityra_Sub_7814",
                    userId = "usr_priya_66",
                    userName = "Priya Nambiar",
                    userPhone = "+91 98451 33221",
                    userEmail = "priya.n@designstudio.co",
                    amount = 49,
                    paymentMethod = "UPI (PhonePe)",
                    status = "SUCCESS",
                    planName = "Mityra VIP Club (₹49/mo)",
                    razorpaySignature = "rzp_sig_ff192804b901ac",
                    timestamp = System.currentTimeMillis() - 3600000 * 36
                )
            )
        }
    }

    fun getUserProfile(): Flow<UserProfileEntity?> = userProfileDao.getUserProfile()

    suspend fun saveUserProfile(profile: UserProfileEntity) {
        userProfileDao.saveUserProfile(profile)
    }

    suspend fun submitCompanionApplication(application: CompanionApplicationEntity) {
        adminDao.insertApplication(application)
    }

    suspend fun approveApplication(applicationId: String, adminNotes: String = "Approved by Operations Admin") {
        adminDao.updateApplicationStatus(
            id = applicationId,
            status = "APPROVED",
            notes = adminNotes,
            reviewedAt = System.currentTimeMillis()
        )
        // If it's a registered user, dynamically ensure they appear in customCompanions
        val app = adminDao.getApplicationById(applicationId)
        if (app != null) {
            val genderEnum = when (app.gender.lowercase()) {
                "boy", "male" -> CompanionGender.BOY
                "girl", "female" -> CompanionGender.GIRL
                else -> CompanionGender.ANY
            }
            val categoryEnum = when (app.primaryCategory.lowercase()) {
                "movie", "movie partner", "movie partners" -> EventCategory.MOVIE_PARTNER
                "event", "event companion", "event companions" -> EventCategory.EVENT_COMPANION
                "party", "party partner", "party partners" -> EventCategory.PARTY_PARTNER
                "hangout", "casual hangout", "casual hangouts" -> EventCategory.CASUAL_HANGOUT
                else -> EventCategory.DINNER_DATE
            }
            val comp = Companion(
                id = "approved_${app.id}",
                name = app.name,
                age = app.age,
                gender = genderEnum,
                city = app.city,
                neighborhood = app.neighborhood,
                primaryCategory = categoryEnum,
                hourlyRate = app.hourlyRate,
                rating = 5.0f,
                reviewCount = 1,
                bio = app.bio,
                interests = app.interests.split(",").map { it.trim() }.filter { it.isNotBlank() },
                languages = app.languages.split(",").map { it.trim() }.filter { it.isNotBlank() },
                boundaries = app.boundaries.split(",").map { it.trim() }.filter { it.isNotBlank() },
                imageResId = R.drawable.img_comp_kabir,
                isVerified = true,
                verificationInfo = VerificationInfo(
                    isGovtIdVerified = true,
                    isBiometricChecked = true,
                    isPoliceClearanceValid = true,
                    badgeLevel = "Mityra Shield Level 3 (Verified Partner)"
                ),
                reviews = listOf(
                    Review("Mityra Operations", 5.0f, "Today", "Approved via Admin Background Checks and Document Verification.", "Partner Induction")
                )
            )
            registerCustomCompanion(comp)
        }
    }

    suspend fun rejectApplication(applicationId: String, reason: String = "Rejected by Admin Operations") {
        adminDao.updateApplicationStatus(
            id = applicationId,
            status = "REJECTED",
            notes = reason,
            reviewedAt = System.currentTimeMillis()
        )
        // Remove from custom companions if present
        _customCompanions.value = _customCompanions.value.filterNot { it.id == "approved_$applicationId" }
    }

    suspend fun recordRazorpayMembershipPayment(payment: MembershipPaymentEntity) {
        adminDao.insertPayment(payment)
        // Update user profile membership status
        val currentProfile = userProfileDao.getUserProfileOnce()
        if (currentProfile != null) {
            val updated = currentProfile.copy(
                hasActiveMembership = true,
                membershipPaymentId = payment.paymentId,
                membershipExpiry = payment.validUntil
            )
            userProfileDao.saveUserProfile(updated)
        }
    }
}

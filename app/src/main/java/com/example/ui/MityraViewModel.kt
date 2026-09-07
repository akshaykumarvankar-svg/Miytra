package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.BookingEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.model.Companion
import com.example.data.model.CompanionApplicationEntity
import com.example.data.model.CompanionGender
import com.example.data.model.EventCategory
import com.example.data.model.MembershipPaymentEntity
import com.example.data.model.Review
import com.example.data.model.UserProfileEntity
import com.example.data.model.UserRole
import com.example.data.model.VerificationInfo
import com.example.data.repository.MityraRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenTab {
    EXPLORE,
    BOOKINGS,
    SAFETY,
    PROFILE
}

class MityraViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    val repository = MityraRepository(
        database.bookingDao(),
        database.chatDao(),
        database.userProfileDao(),
        database.adminDao()
    )

    // Navigation & Tabs
    private val _currentTab = MutableStateFlow(ScreenTab.EXPLORE)
    val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

    // Selected Screens & Overlays
    private val _selectedCompanion = MutableStateFlow<Companion?>(null)
    val selectedCompanion: StateFlow<Companion?> = _selectedCompanion.asStateFlow()

    private val _bookingCompanion = MutableStateFlow<Companion?>(null)
    val bookingCompanion: StateFlow<Companion?> = _bookingCompanion.asStateFlow()

    private val _chatCompanion = MutableStateFlow<Companion?>(null)
    val chatCompanion: StateFlow<Companion?> = _chatCompanion.asStateFlow()

    private val _verifiedBadgeCompanion = MutableStateFlow<Companion?>(null)
    val verifiedBadgeCompanion: StateFlow<Companion?> = _verifiedBadgeCompanion.asStateFlow()

    private val _isSosModalOpen = MutableStateFlow(false)
    val isSosModalOpen: StateFlow<Boolean> = _isSosModalOpen.asStateFlow()

    private val _isFakeCallActive = MutableStateFlow(false)
    val isFakeCallActive: StateFlow<Boolean> = _isFakeCallActive.asStateFlow()

    private val _isPaymentScreenOpen = MutableStateFlow(false)
    val isPaymentScreenOpen: StateFlow<Boolean> = _isPaymentScreenOpen.asStateFlow()

    private val _isPaymentSuccess = MutableStateFlow(false)
    val isPaymentSuccess: StateFlow<Boolean> = _isPaymentSuccess.asStateFlow()

    private val _isSelfRegisterOpen = MutableStateFlow(false)
    val isSelfRegisterOpen: StateFlow<Boolean> = _isSelfRegisterOpen.asStateFlow()

    // Authentication & Role-Based Access Control
    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    private val _currentUserRole = MutableStateFlow(UserRole.MEMBER)
    val currentUserRole: StateFlow<UserRole> = _currentUserRole.asStateFlow()

    private val _authScreenMode = MutableStateFlow("LOGIN") // "LOGIN", "SIGNUP", "ADMIN_LOGIN"
    val authScreenMode: StateFlow<String> = _authScreenMode.asStateFlow()

    private val _authLoginMethod = MutableStateFlow("MOBILE") // "MOBILE" or "EMAIL"
    val authLoginMethod: StateFlow<String> = _authLoginMethod.asStateFlow()

    private val _authSelectedRole = MutableStateFlow(UserRole.MEMBER)
    val authSelectedRole: StateFlow<UserRole> = _authSelectedRole.asStateFlow()

    private val _authPhoneInput = MutableStateFlow("+91 98201 44892")
    val authPhoneInput: StateFlow<String> = _authPhoneInput.asStateFlow()

    private val _authOtpInput = MutableStateFlow("")
    val authOtpInput: StateFlow<String> = _authOtpInput.asStateFlow()

    private val _isOtpSent = MutableStateFlow(false)
    val isOtpSent: StateFlow<Boolean> = _isOtpSent.asStateFlow()

    private val _authEmailInput = MutableStateFlow("rohan.sharma@example.com")
    val authEmailInput: StateFlow<String> = _authEmailInput.asStateFlow()

    private val _authPasswordInput = MutableStateFlow("pass123")
    val authPasswordInput: StateFlow<String> = _authPasswordInput.asStateFlow()

    private val _authNameInput = MutableStateFlow("")
    val authNameInput: StateFlow<String> = _authNameInput.asStateFlow()

    private val _authCityInput = MutableStateFlow("Mumbai")
    val authCityInput: StateFlow<String> = _authCityInput.asStateFlow()

    private val _authErrorMessage = MutableStateFlow<String?>(null)
    val authErrorMessage: StateFlow<String?> = _authErrorMessage.asStateFlow()

    private val _authSuccessMessage = MutableStateFlow<String?>(null)
    val authSuccessMessage: StateFlow<String?> = _authSuccessMessage.asStateFlow()

    val allRegisteredUsers: StateFlow<List<UserProfileEntity>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Admin Panel States
    private val _isAdminPanelOpen = MutableStateFlow(false)
    val isAdminPanelOpen: StateFlow<Boolean> = _isAdminPanelOpen.asStateFlow()

    private val _adminFilterStatus = MutableStateFlow("ALL") // "ALL", "PENDING", "APPROVED", "REJECTED"
    val adminFilterStatus: StateFlow<String> = _adminFilterStatus.asStateFlow()

    val companionApplications: StateFlow<List<CompanionApplicationEntity>> = repository.companionApplications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val membershipPayments: StateFlow<List<MembershipPaymentEntity>> = repository.membershipPayments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Razorpay Gateway States (₹49/month VIP Membership)
    private val _isRazorpayModalOpen = MutableStateFlow(false)
    val isRazorpayModalOpen: StateFlow<Boolean> = _isRazorpayModalOpen.asStateFlow()

    private val _razorpaySelectedMethod = MutableStateFlow("UPI") // "UPI", "CARD", "NETBANKING", "WALLET"
    val razorpaySelectedMethod: StateFlow<String> = _razorpaySelectedMethod.asStateFlow()

    private val _razorpayUpiApp = MutableStateFlow("Google Pay")
    val razorpayUpiApp: StateFlow<String> = _razorpayUpiApp.asStateFlow()

    private val _razorpayCustomVpa = MutableStateFlow("")
    val razorpayCustomVpa: StateFlow<String> = _razorpayCustomVpa.asStateFlow()

    private val _razorpayCardNumber = MutableStateFlow("")
    val razorpayCardNumber: StateFlow<String> = _razorpayCardNumber.asStateFlow()

    private val _razorpayCardExpiry = MutableStateFlow("")
    val razorpayCardExpiry: StateFlow<String> = _razorpayCardExpiry.asStateFlow()

    private val _razorpayCardCvv = MutableStateFlow("")
    val razorpayCardCvv: StateFlow<String> = _razorpayCardCvv.asStateFlow()

    private val _razorpaySelectedBank = MutableStateFlow("HDFC Bank")
    val razorpaySelectedBank: StateFlow<String> = _razorpaySelectedBank.asStateFlow()

    private val _isRazorpayProcessing = MutableStateFlow(false)
    val isRazorpayProcessing: StateFlow<Boolean> = _isRazorpayProcessing.asStateFlow()

    private val _razorpaySuccessPayment = MutableStateFlow<MembershipPaymentEntity?>(null)
    val razorpaySuccessPayment: StateFlow<MembershipPaymentEntity?> = _razorpaySuccessPayment.asStateFlow()

    // User Profile from Room Database
    val userProfile: StateFlow<UserProfileEntity?> = repository.getUserProfile()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // Filters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(EventCategory.ALL)
    val selectedCategory: StateFlow<EventCategory> = _selectedCategory.asStateFlow()

    private val _selectedGender = MutableStateFlow(CompanionGender.ANY)
    val selectedGender: StateFlow<CompanionGender> = _selectedGender.asStateFlow()

    private val _selectedCity = MutableStateFlow("All Cities")
    val selectedCity: StateFlow<String> = _selectedCity.asStateFlow()

    private val _maxHourlyRate = MutableStateFlow(1500)
    val maxHourlyRate: StateFlow<Int> = _maxHourlyRate.asStateFlow()

    val allCompanions: StateFlow<List<Companion>> = MutableStateFlow(repository.companionsList).asStateFlow()

    // Filtered Companions Flow (combining preloaded and user self-registered companions)
    val filteredCompanions: StateFlow<List<Companion>> = combine(
        repository.customCompanions,
        _searchQuery,
        _selectedCategory,
        _selectedGender,
        combine(_selectedCity, _maxHourlyRate) { city, rate -> city to rate }
    ) { customComps, query, category, gender, cityAndRate ->
        val (city, maxRate) = cityAndRate
        val fullList = repository.companionsList + customComps
        fullList.filter { companion ->
            val matchesQuery = query.isBlank() ||
                    companion.name.contains(query, ignoreCase = true) ||
                    companion.interests.any { it.contains(query, ignoreCase = true) } ||
                    companion.neighborhood.contains(query, ignoreCase = true) ||
                    companion.bio.contains(query, ignoreCase = true)

            val matchesCategory = category == EventCategory.ALL ||
                    companion.primaryCategory == category

            val matchesGender = gender == CompanionGender.ANY ||
                    companion.gender == gender

            val matchesCity = city == "All Cities" ||
                    companion.city.equals(city, ignoreCase = true)

            val matchesRate = companion.hourlyRate <= maxRate

            matchesQuery && matchesCategory && matchesGender && matchesCity && matchesRate
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = repository.companionsList
    )

    // Bookings from Room Database
    val activeBookings: StateFlow<List<BookingEntity>> = repository.getAllBookings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Active Chat Messages
    val chatMessages: StateFlow<List<ChatMessageEntity>> = _chatCompanion
        .flatMapLatest { comp ->
            if (comp != null) repository.getChatMessages(comp.id)
            else flowOf(emptyList())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Booking Flow Form State
    private val _bookingEventType = MutableStateFlow("Dinner Dates 🍷")
    val bookingEventType: StateFlow<String> = _bookingEventType.asStateFlow()

    private val _bookingDate = MutableStateFlow("Tonight, 8:00 PM")
    val bookingDate: StateFlow<String> = _bookingDate.asStateFlow()

    private val _bookingTimeSlot = MutableStateFlow("08:00 PM - 11:00 PM")
    val bookingTimeSlot: StateFlow<String> = _bookingTimeSlot.asStateFlow()

    private val _bookingDurationHours = MutableStateFlow(3)
    val bookingDurationHours: StateFlow<Int> = _bookingDurationHours.asStateFlow()

    private val _bookingVenueAddress = MutableStateFlow("Bastian at the Top, Bandra West, Mumbai")
    val bookingVenueAddress: StateFlow<String> = _bookingVenueAddress.asStateFlow()

    private val _bookingSpecialNotes = MutableStateFlow("Platonic dinner outing. Smart casual attire.")
    val bookingSpecialNotes: StateFlow<String> = _bookingSpecialNotes.asStateFlow()

    private val _appliedCoupon = MutableStateFlow("MITYRA200")
    val appliedCoupon: StateFlow<String> = _appliedCoupon.asStateFlow()

    private val _discountAmount = MutableStateFlow(200)
    val discountAmount: StateFlow<Int> = _discountAmount.asStateFlow()

    // Payment Form State
    private val _selectedPaymentMethod = MutableStateFlow("UPI")
    val selectedPaymentMethod: StateFlow<String> = _selectedPaymentMethod.asStateFlow()

    private val _selectedUpiApp = MutableStateFlow("Google Pay")
    val selectedUpiApp: StateFlow<String> = _selectedUpiApp.asStateFlow()

    private val _cardNumber = MutableStateFlow("4532 •••• •••• 8821")
    val cardNumber: StateFlow<String> = _cardNumber.asStateFlow()

    private val _cardExpiry = MutableStateFlow("08/29")
    val cardExpiry: StateFlow<String> = _cardExpiry.asStateFlow()

    private val _cardCvv = MutableStateFlow("421")
    val cardCvv: StateFlow<String> = _cardCvv.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    // Navigation Actions
    fun selectTab(tab: ScreenTab) {
        _currentTab.value = tab
    }

    fun selectCompanion(companion: Companion?) {
        _selectedCompanion.value = companion
    }

    fun startBookingFlow(companion: Companion) {
        _bookingCompanion.value = companion
        _bookingEventType.value = "${companion.primaryCategory.emoji} ${companion.primaryCategory.title}"
        _isPaymentScreenOpen.value = false
        _isPaymentSuccess.value = false
    }

    fun closeBookingFlow() {
        _bookingCompanion.value = null
        _isPaymentScreenOpen.value = false
        _isPaymentSuccess.value = false
    }

    fun proceedToPayment() {
        _isPaymentSuccess.value = false
        _isPaymentScreenOpen.value = true
    }

    fun closePaymentScreen() {
        _isPaymentScreenOpen.value = false
    }

    fun confirmPayment() {
        val companion = _bookingCompanion.value ?: return
        viewModelScope.launch {
            val baseCost = companion.hourlyRate * _bookingDurationHours.value
            val platformFee = 99
            val gst = (baseCost * 0.18f).toInt()
            val discount = _discountAmount.value
            val total = (baseCost + platformFee + gst - discount).coerceAtLeast(0)

            val booking = BookingEntity(
                bookingReference = "MIT-${(10000..99999).random()}",
                companionId = companion.id,
                companionName = companion.name,
                companionAge = companion.age,
                companionPhotoRes = companion.imageResId,
                eventType = _bookingEventType.value,
                date = _bookingDate.value,
                timeSlot = _bookingTimeSlot.value,
                durationHours = _bookingDurationHours.value,
                hourlyRate = companion.hourlyRate,
                baseAmount = baseCost,
                platformFee = platformFee,
                taxAmount = gst,
                discountAmount = discount,
                totalAmount = total,
                paymentMethod = when (_selectedPaymentMethod.value) {
                    "UPI" -> "UPI (${_selectedUpiApp.value})"
                    "CARD" -> "Card ending in 8821"
                    else -> "Mityra Wallet"
                },
                venueAddress = _bookingVenueAddress.value,
                specialNotes = _bookingSpecialNotes.value,
                status = "CONFIRMED"
            )

            repository.saveBooking(booking)
            _isPaymentSuccess.value = true
        }
    }

    fun openChat(companion: Companion) {
        _chatCompanion.value = companion
    }

    fun closeChat() {
        _chatCompanion.value = null
    }

    fun sendChatMessage(text: String) {
        val companion = _chatCompanion.value ?: return
        if (text.isNotBlank()) {
            viewModelScope.launch {
                repository.sendMessage(companion.id, text.trim())
            }
        }
    }

    // Safety & SOS Modals
    fun openSosModal() {
        _isSosModalOpen.value = true
    }

    fun closeSosModal() {
        _isSosModalOpen.value = false
    }

    fun triggerFakeCall() {
        _isSosModalOpen.value = false
        _isFakeCallActive.value = true
    }

    fun dismissFakeCall() {
        _isFakeCallActive.value = false
    }

    fun openVerifiedBadgeDetails(companion: Companion) {
        _verifiedBadgeCompanion.value = companion
    }

    fun closeVerifiedBadgeDetails() {
        _verifiedBadgeCompanion.value = null
    }

    // Filters Actions
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: EventCategory) {
        _selectedCategory.value = category
    }

    fun setGender(gender: CompanionGender) {
        _selectedGender.value = gender
    }

    fun setCity(city: String) {
        _selectedCity.value = city
    }

    fun setMaxHourlyRate(rate: Int) {
        _maxHourlyRate.value = rate
    }

    // Booking Form Setters
    fun setBookingEventType(type: String) {
        _bookingEventType.value = type
    }

    fun setBookingDate(date: String) {
        _bookingDate.value = date
    }

    fun setBookingTimeSlot(slot: String) {
        _bookingTimeSlot.value = slot
    }

    fun setBookingDurationHours(hours: Int) {
        _bookingDurationHours.value = hours
    }

    fun setBookingVenueAddress(address: String) {
        _bookingVenueAddress.value = address
    }

    fun setBookingSpecialNotes(notes: String) {
        _bookingSpecialNotes.value = notes
    }

    fun applyCoupon(code: String) {
        if (code.equals("MITYRA200", ignoreCase = true) || code.equals("FIRST50", ignoreCase = true)) {
            _appliedCoupon.value = code.uppercase()
            _discountAmount.value = 200
        } else {
            _appliedCoupon.value = ""
            _discountAmount.value = 0
        }
    }

    fun setPaymentMethod(method: String) {
        _selectedPaymentMethod.value = method
    }

    fun setUpiApp(app: String) {
        _selectedUpiApp.value = app
    }

    fun setCardNumber(number: String) {
        _cardNumber.value = number
    }

    fun setCardExpiry(expiry: String) {
        _cardExpiry.value = expiry
    }

    fun setCardCvv(cvv: String) {
        _cardCvv.value = cvv
    }

    fun openSelfRegister() {
        _isSelfRegisterOpen.value = true
    }

    fun closeSelfRegister() {
        _isSelfRegisterOpen.value = false
    }

    // Admin Panel Controls
    fun openAdminPanel() {
        _isAdminPanelOpen.value = true
    }

    fun closeAdminPanel() {
        _isAdminPanelOpen.value = false
    }

    fun setAdminFilterStatus(status: String) {
        _adminFilterStatus.value = status
    }

    fun approveCompanionApplication(applicationId: String, notes: String = "Approved by Admin Operations") {
        viewModelScope.launch {
            repository.approveApplication(applicationId, notes)
        }
    }

    fun rejectCompanionApplication(applicationId: String, reason: String = "Application does not meet safety criteria") {
        viewModelScope.launch {
            repository.rejectApplication(applicationId, reason)
        }
    }

    // Razorpay Flow Controls (₹49/month VIP Membership)
    fun openRazorpayCheckout() {
        _isRazorpayProcessing.value = false
        _razorpaySuccessPayment.value = null
        _isRazorpayModalOpen.value = true
    }

    fun closeRazorpayCheckout() {
        _isRazorpayModalOpen.value = false
        _isRazorpayProcessing.value = false
    }

    fun setRazorpayMethod(method: String) {
        _razorpaySelectedMethod.value = method
    }

    fun setRazorpayUpiApp(app: String) {
        _razorpayUpiApp.value = app
    }

    fun setRazorpayCustomVpa(vpa: String) {
        _razorpayCustomVpa.value = vpa
    }

    fun setRazorpayCardDetails(number: String, expiry: String, cvv: String) {
        _razorpayCardNumber.value = number
        _razorpayCardExpiry.value = expiry
        _razorpayCardCvv.value = cvv
    }

    fun setRazorpayBank(bank: String) {
        _razorpaySelectedBank.value = bank
    }

    fun confirmRazorpayPayment() {
        viewModelScope.launch {
            _isRazorpayProcessing.value = true
            kotlinx.coroutines.delay(1500)

            val user = userProfile.value
            val userName = user?.name ?: "Guest User"
            val userPhone = user?.phone ?: "+91 98201 44892"
            val userEmail = user?.email ?: "guest.user@example.com"
            val paymentId = "pay_Rzp${System.currentTimeMillis().toString().takeLast(8)}"
            val orderId = "order_Mityra_${System.currentTimeMillis().toString().takeLast(6)}"

            val methodLabel = when (_razorpaySelectedMethod.value) {
                "UPI" -> "UPI (${_razorpayUpiApp.value})"
                "CARD" -> "Card (•••• ${if (_razorpayCardNumber.value.length >= 4) _razorpayCardNumber.value.takeLast(4) else "4242"})"
                "NETBANKING" -> "Netbanking (${_razorpaySelectedBank.value})"
                else -> "UPI Autopay"
            }

            val paymentRecord = MembershipPaymentEntity(
                paymentId = paymentId,
                orderId = orderId,
                userId = user?.id ?: "primary_user",
                userName = userName,
                userPhone = userPhone,
                userEmail = userEmail,
                amount = 49,
                currency = "INR",
                paymentMethod = methodLabel,
                status = "SUCCESS",
                planName = "Mityra VIP Club (₹49/mo)",
                razorpaySignature = "rzp_sig_${java.util.UUID.randomUUID().toString().replace("-", "").take(16)}",
                timestamp = System.currentTimeMillis(),
                validUntil = System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000
            )

            repository.recordRazorpayMembershipPayment(paymentRecord)
            _isRazorpayProcessing.value = false
            _razorpaySuccessPayment.value = paymentRecord
        }
    }

    fun saveUserProfile(profile: UserProfileEntity, registerAsCompanion: Boolean = false) {
        viewModelScope.launch {
            repository.saveUserProfile(profile)
            if (registerAsCompanion) {
                val newAppId = "app_${System.currentTimeMillis()}"
                val newApp = CompanionApplicationEntity(
                    id = newAppId,
                    userId = profile.id,
                    name = profile.name,
                    age = profile.age,
                    gender = profile.gender,
                    city = profile.city,
                    neighborhood = profile.neighborhood,
                    phone = profile.phone,
                    email = profile.email,
                    bio = profile.bio,
                    interests = profile.interests,
                    languages = profile.languages,
                    primaryCategory = profile.primaryCategory,
                    hourlyRate = profile.hourlyRate,
                    boundaries = profile.boundaries,
                    kycDocumentType = profile.kycDocumentType,
                    kycIdMasked = profile.kycIdMasked,
                    isKycVerified = profile.isKycVerified,
                    status = "APPROVED",
                    adminNotes = "Instant partner activation with Aadhaar/ID verification.",
                    submittedAt = System.currentTimeMillis(),
                    reviewedAt = System.currentTimeMillis()
                )
                repository.submitCompanionApplication(newApp)

                val newCompanion = Companion(
                    id = "custom_comp_${System.currentTimeMillis()}",
                    name = profile.name,
                    age = profile.age,
                    gender = when (profile.gender.lowercase()) {
                        "female", "girl" -> CompanionGender.GIRL
                        "male", "boy" -> CompanionGender.BOY
                        else -> CompanionGender.ANY
                    },
                    city = profile.city,
                    neighborhood = profile.neighborhood,
                    primaryCategory = when (profile.primaryCategory.lowercase()) {
                        "movie", "movie partner", "movie partners" -> EventCategory.MOVIE_PARTNER
                        "event", "event companion", "event companions" -> EventCategory.EVENT_COMPANION
                        "party", "party partner", "party partners" -> EventCategory.PARTY_PARTNER
                        "hangout", "casual hangout", "casual hangouts" -> EventCategory.CASUAL_HANGOUT
                        else -> EventCategory.DINNER_DATE
                    },
                    hourlyRate = profile.hourlyRate,
                    rating = 5.0f,
                    reviewCount = 1,
                    bio = profile.bio,
                    interests = profile.interests.split(",").map { it.trim() }.filter { it.isNotBlank() },
                    languages = profile.languages.split(",").map { it.trim() }.filter { it.isNotBlank() },
                    boundaries = profile.boundaries.split(",").map { it.trim() }.filter { it.isNotBlank() },
                    imageResId = com.example.R.drawable.img_comp_tanya,
                    isVerified = profile.isKycVerified,
                    verificationInfo = VerificationInfo(
                        isGovtIdVerified = profile.isKycVerified,
                        isBiometricChecked = profile.isKycVerified,
                        isPoliceClearanceValid = profile.isKycVerified,
                        badgeLevel = "Mityra Shield Level 3 (Verified Host)"
                    ),
                    reviews = listOf(
                        Review("Mityra Team", 5.0f, "Today", "Welcome to Mityra! Profile authenticated via KYC verification.", "Onboarding")
                    )
                )
                repository.registerCustomCompanion(newCompanion)
            }
            _isSelfRegisterOpen.value = false
        }
    }

    // Authentication Setters and Actions
    fun setAuthScreenMode(mode: String) {
        _authScreenMode.value = mode
        _authErrorMessage.value = null
        _authSuccessMessage.value = null
    }

    fun setAuthLoginMethod(method: String) {
        _authLoginMethod.value = method
        _authErrorMessage.value = null
    }

    fun setAuthSelectedRole(role: UserRole) {
        _authSelectedRole.value = role
        if (role == UserRole.ADMIN) {
            _authEmailInput.value = "admin@mityra.com"
            _authPasswordInput.value = "admin123"
        }
    }

    fun setAuthPhoneInput(phone: String) {
        _authPhoneInput.value = phone
    }

    fun setAuthOtpInput(otp: String) {
        _authOtpInput.value = otp
    }

    fun setAuthEmailInput(email: String) {
        _authEmailInput.value = email
    }

    fun setAuthPasswordInput(password: String) {
        _authPasswordInput.value = password
    }

    fun setAuthNameInput(name: String) {
        _authNameInput.value = name
    }

    fun setAuthCityInput(city: String) {
        _authCityInput.value = city
    }

    fun clearAuthMessages() {
        _authErrorMessage.value = null
        _authSuccessMessage.value = null
    }

    fun sendOtp() {
        val phone = _authPhoneInput.value.trim()
        if (phone.length < 10) {
            _authErrorMessage.value = "Please enter a valid 10-digit mobile number"
            return
        }
        _isOtpSent.value = true
        _authSuccessMessage.value = "OTP sent to $phone (Demo code: 123456)"
        _authErrorMessage.value = null
    }

    fun autoFillOtp() {
        _authOtpInput.value = "123456"
        _authErrorMessage.value = null
    }

    fun verifyOtpAndLogin() {
        val phone = _authPhoneInput.value.trim()
        val otp = _authOtpInput.value.trim()
        if (otp.length != 6) {
            _authErrorMessage.value = "Please enter the 6-digit OTP"
            return
        }
        if (otp != "123456" && otp != "000000") {
            _authErrorMessage.value = "Invalid OTP code. Use demo code: 123456"
            return
        }

        viewModelScope.launch {
            val role = _authSelectedRole.value
            if (role == UserRole.ADMIN) {
                _isAdminLoggedIn.value = true
                _isAdminPanelOpen.value = true
                _isLoggedIn.value = true
                _currentUserRole.value = UserRole.ADMIN
                _authErrorMessage.value = null
                return@launch
            }

            val existing = repository.getUserByPhone(phone)
            if (existing != null) {
                _isLoggedIn.value = true
                _currentUserRole.value = when (existing.role.uppercase()) {
                    "COMPANION" -> UserRole.COMPANION
                    "ADMIN" -> UserRole.ADMIN
                    else -> UserRole.MEMBER
                }
            } else {
                val newProfile = UserProfileEntity(
                    id = "primary_user",
                    name = if (role == UserRole.COMPANION) "Verified Companion" else "Mityra Member",
                    role = role.name,
                    phone = phone,
                    email = "member.${phone.takeLast(4)}@mityra.com",
                    hasActiveMembership = (role == UserRole.MEMBER)
                )
                repository.saveUserProfile(newProfile)
                _isLoggedIn.value = true
                _currentUserRole.value = role
            }
            _authErrorMessage.value = null
            _isOtpSent.value = false
            _authOtpInput.value = ""
        }
    }

    fun loginWithEmailPassword() {
        val email = _authEmailInput.value.trim()
        val password = _authPasswordInput.value.trim()
        val role = _authSelectedRole.value

        if (email.isBlank() || !email.contains("@")) {
            _authErrorMessage.value = "Please enter a valid email address"
            return
        }
        if (password.isBlank()) {
            _authErrorMessage.value = "Please enter your password"
            return
        }

        // Admin check: admin@mityra.com / admin123
        if (role == UserRole.ADMIN || email.equals("admin@mityra.com", ignoreCase = true)) {
            if (email.equals("admin@mityra.com", ignoreCase = true) && password == "admin123") {
                _isAdminLoggedIn.value = true
                _isAdminPanelOpen.value = true
                _isLoggedIn.value = true
                _currentUserRole.value = UserRole.ADMIN
                _authErrorMessage.value = null
                return
            } else {
                _authErrorMessage.value = "Invalid Admin credentials! Use demo: admin@mityra.com / admin123"
                return
            }
        }

        viewModelScope.launch {
            val existing = repository.getUserByEmail(email)
            if (existing != null) {
                if (existing.password == password || password == "pass123") {
                    _isLoggedIn.value = true
                    _currentUserRole.value = when (existing.role.uppercase()) {
                        "COMPANION" -> UserRole.COMPANION
                        "ADMIN" -> UserRole.ADMIN
                        else -> UserRole.MEMBER
                    }
                    _authErrorMessage.value = null
                } else {
                    _authErrorMessage.value = "Incorrect password! Default demo password is pass123"
                }
            } else {
                val newProfile = UserProfileEntity(
                    id = "primary_user",
                    name = email.substringBefore("@").replace(".", " ").replaceFirstChar { it.uppercase() },
                    role = role.name,
                    email = email,
                    password = password,
                    hasActiveMembership = (role == UserRole.MEMBER)
                )
                repository.saveUserProfile(newProfile)
                _isLoggedIn.value = true
                _currentUserRole.value = role
                _authErrorMessage.value = null
            }
        }
    }

    fun signupUser() {
        val name = _authNameInput.value.trim()
        val email = _authEmailInput.value.trim()
        val phone = _authPhoneInput.value.trim()
        val password = _authPasswordInput.value.trim()
        val role = _authSelectedRole.value
        val city = _authCityInput.value.trim().ifBlank { "Mumbai" }

        if (name.isBlank()) {
            _authErrorMessage.value = "Please enter your full name"
            return
        }
        if (email.isBlank() || !email.contains("@")) {
            _authErrorMessage.value = "Please enter a valid email"
            return
        }
        if (phone.length < 10) {
            _authErrorMessage.value = "Please enter a valid phone number"
            return
        }
        if (password.length < 4) {
            _authErrorMessage.value = "Password must be at least 4 characters"
            return
        }

        viewModelScope.launch {
            val newProfile = UserProfileEntity(
                id = "primary_user",
                name = name,
                role = role.name,
                email = email,
                phone = phone,
                city = city,
                password = password,
                hasActiveMembership = true,
                membershipPaymentId = "pay_RzpSignup_${System.currentTimeMillis().toString().takeLast(6)}"
            )
            repository.saveUserProfile(newProfile)
            _isLoggedIn.value = true
            _currentUserRole.value = role
            _authErrorMessage.value = null
            _authSuccessMessage.value = "Account created successfully! Welcome to Mityra."
        }
    }

    fun quickFillAdminCredentials() {
        _authSelectedRole.value = UserRole.ADMIN
        _authEmailInput.value = "admin@mityra.com"
        _authPasswordInput.value = "admin123"
        _authLoginMethod.value = "EMAIL"
        _authErrorMessage.value = null
    }

    fun quickDemoLogin(role: UserRole) {
        viewModelScope.launch {
            when (role) {
                UserRole.ADMIN -> {
                    _isAdminLoggedIn.value = true
                    _isAdminPanelOpen.value = true
                    _isLoggedIn.value = true
                    _currentUserRole.value = UserRole.ADMIN
                }
                UserRole.COMPANION -> {
                    _isLoggedIn.value = true
                    _isAdminLoggedIn.value = false
                    _isAdminPanelOpen.value = false
                    _currentUserRole.value = UserRole.COMPANION
                    val profile = repository.getUserProfile().firstOrNull() ?: UserProfileEntity(
                        name = "Priya Nambiar",
                        role = "COMPANION",
                        phone = "+91 98451 33221",
                        email = "priya.n@designstudio.co",
                        hasActiveMembership = true
                    )
                    repository.saveUserProfile(profile.copy(role = "COMPANION"))
                }
                UserRole.MEMBER -> {
                    _isLoggedIn.value = true
                    _isAdminLoggedIn.value = false
                    _isAdminPanelOpen.value = false
                    _currentUserRole.value = UserRole.MEMBER
                    val profile = repository.getUserProfile().firstOrNull() ?: UserProfileEntity(
                        name = "Rohan Sharma",
                        role = "MEMBER",
                        phone = "+91 98201 44892",
                        email = "rohan.sharma@example.com",
                        hasActiveMembership = true
                    )
                    repository.saveUserProfile(profile.copy(role = "MEMBER"))
                }
            }
            _authErrorMessage.value = null
        }
    }

    fun logout() {
        _isLoggedIn.value = false
        _isAdminLoggedIn.value = false
        _isAdminPanelOpen.value = false
        _authScreenMode.value = "LOGIN"
        _authErrorMessage.value = null
        _authSuccessMessage.value = "Logged out successfully"
    }

    fun logoutAdmin() {
        _isAdminLoggedIn.value = false
        _isAdminPanelOpen.value = false
        _isLoggedIn.value = false
        _authScreenMode.value = "LOGIN"
        _authErrorMessage.value = null
        _authSuccessMessage.value = "Admin session ended"
    }
}

class MityraViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MityraViewModel::class.java)) {
            val app = context.applicationContext as Application
            @Suppress("UNCHECKED_CAST")
            return MityraViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

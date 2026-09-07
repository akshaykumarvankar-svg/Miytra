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
import com.example.data.model.CompanionGender
import com.example.data.model.EventCategory
import com.example.data.model.Review
import com.example.data.model.UserProfileEntity
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
    val repository = MityraRepository(database.bookingDao(), database.chatDao(), database.userProfileDao())

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

    fun saveUserProfile(profile: UserProfileEntity, registerAsCompanion: Boolean = false) {
        viewModelScope.launch {
            repository.saveUserProfile(profile)
            if (registerAsCompanion) {
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
